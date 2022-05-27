package org.hemmerit.riss.app;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map;
import java.util.HashMap;
import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.hemmerit.riss.datamodel.atom.Entry;
import org.hemmerit.riss.datamodel.atom.Feed;
import org.hemmerit.riss.datamodel.opml.Body;
import org.hemmerit.riss.datamodel.opml.Head;
import org.hemmerit.riss.datamodel.opml.Opml;
import org.hemmerit.riss.datamodel.opml.Outline;
import org.hemmerit.riss.datamodel.rss.Channel;
import org.hemmerit.riss.datamodel.rss.Item;
import org.hemmerit.riss.datamodel.rss.Rss;

public class RissMain {

    public static JAXBContext jaxbContext = null;
    public static Unmarshaller jaxbUnmarshaller = null;

    private static final Logger logger = Logger.getLogger(RissMain.class.getName());
    private static FileHandler fh = null;
    private static File feedsFile = null;

    private static final PopupMenu popup = new PopupMenu();
    private static final TrayIcon trayIcon = new TrayIcon(createImage("120px-RSS-Ikona.png", "tray icon"));
    private static final SystemTray tray = SystemTray.getSystemTray();

    private static final Map<String, Menu> menuRegistry = new HashMap<>();;
        

    public static void main(String[] args) throws Exception {
        // Launch the config dialog
        // If there is no RiSS data directory, create it in users home directory
        // On Windows machines this would point to e.g. C: Users username RiSS
        Path rissDirectory = Paths.get(System.getProperty("user.home") + File.separator + "RiSS");
        Path opmlFile = rissDirectory.resolve("feeds.opml");
        if (Files.notExists(opmlFile)) {
            if (!Files.exists(rissDirectory)) {
                Files.createDirectory(rissDirectory);
            }
            InputStream src = RissMain.class.getResourceAsStream("/feeds.opml");
            Files.copy(src, opmlFile);
        }
        feedsFile = opmlFile.toFile();
        // Initiate Logging to file - standard Java Unified Logging is sufficent in this use-case
        fh = new FileHandler(rissDirectory.toString() + File.separator + "RiSS.log");
        logger.addHandler(fh);

        // Initiate the Object-XML mapper with the right classes to parse
        jaxbContext = JAXBContext.newInstance(Rss.class, Item.class, Channel.class, Opml.class, Body.class,
                Outline.class, Feed.class, Entry.class, Head.class);

        // Create the helper for the creation of Objects from XML inputs (Feeds, etc.)
        jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        // Create the look and feel of choice
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException
                | ClassNotFoundException ex) {
            logger.severe("Could not set look and feel to Metal");
        }

        // Tell Metal to not use bold fonts
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        // Schedule a job for the event-dispatching thread:
        // adding TrayIcon.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                try {
                    createAndShowGUI();
                    TimerTask repeatedTask = new TimerTask() {
                        public void run() {
                            logger.info("Updating all Feeds");
                            try {
                                for (String feedUrl : menuRegistry.keySet())
                                {
                                    update(feedUrl, menuRegistry.get(feedUrl));
                                }
                            logger.info("Update complete");
                            } catch (Exception e)
                            {
                                logger.severe("Could not update feeds " +e.getMessage());
                            }   
                        }
                    };
                    Timer timer = new Timer("UpdateTimer");
                    timer.scheduleAtFixedRate(repeatedTask, new Date(), 120000);
                } catch (Exception ex) {
                    logger.severe("Could not create and show GUI" + ex.getMessage());
                }
            }
        });
    }

    private static void createAndShowGUI() throws Exception {
        // Check the SystemTray support
        if (!SystemTray.isSupported()) {
            logger.severe("Program terminated - Operating System does not support tray usage");
            return;
        }
        popup.removeAll();
        if (tray.getTrayIcons().length>0)
        {
            for (TrayIcon currentIcon : tray.getTrayIcons())
            {
                tray.remove(currentIcon);
            }
        }
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            logger.severe("Could not add tray icon " + e.getMessage());
            return;
        }
        
        Opml opml = (Opml) jaxbUnmarshaller.unmarshal(new FileInputStream(feedsFile));
        for (Outline outline : opml.body.getOutline()) {
            InputStream is = null;
            try {
                URL url = new URL(outline.getXmlUrl());
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.addRequestProperty("User-Agent", "Mozilla/4.76");
                is = http.getInputStream();
            } catch (Exception conErrorEx) {
                logger.severe("Could not read URL: " + outline.getXmlUrl());
            }
            Menu rssMenu = new Menu(outline.getText());

            // Define what happens, when you click on the Feeds menu item
            rssMenu.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Call the update method below to remove old items and load new ones
                    Menu menu = (Menu) e.getSource();
                    update(outline.getXmlUrl(), menu);
                }
            });

            // update(outline.getXmlUrl(), rssMenu);
            create(outline, rssMenu);
            popup.add(rssMenu);
        }

        popup.add(createExitMenuItem());

        trayIcon.setPopupMenu(popup);   
    }
    
    //Obtain the image URL
    protected static Image createImage(String path, String description) 
    {
        URL imageURL = RissMain.class.getResource("/" +path);
        
        if (imageURL == null) 
        {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }
    
    public static void update(String feedUrl, Menu menu)
    {
        menuRegistry.put(feedUrl, menu);
        // Read the feed as an InputStream (pretending to be a web-browser)
        InputStream is = null;
        try 
        {
            URL url = new URL(feedUrl);
            HttpURLConnection http = (HttpURLConnection) url.openConnection(); 
            http.addRequestProperty("User-Agent", "Mozilla/4.76"); 
            is = http.getInputStream();                
        }
        catch (Exception conErrorEx)
        {
            logger.severe("Could not read URL: " +feedUrl + " " +conErrorEx.getMessage());
        }
        
        // Remove the old news from the view
        menu.removeAll();
        
        try {
            // Now lets make an Object out of the received XML file
            Object feed = jaxbUnmarshaller.unmarshal(is);
                
                // RSS Feeds and modern Atom Feeds need to be handled differently
                if (feed instanceof Rss)
                {
                    Rss rss = (Rss) feed;
                    for (Item item : rss.getChannel().getItem())
                    {
                        menu.add(createNewsMenuItem(item.getTitle(), new URL(item.getLink()).toURI()));
                    }
                }
                else if (feed instanceof Feed)
                {
                    Feed rss = (Feed) feed;

                    for (Entry item : rss.getEntry())
                    {
                        menu.add(createNewsMenuItem(item.getTitle(), new URL(item.getLink().getHref()).toURI()));
                    }
                }
            }
            catch (Exception ex)
            {
                logger.severe(ex.getMessage());
            }
            logger.info("Updated " +feedUrl);
    }

    public static MenuItem createNewsMenuItem(String title, URI url)
    {
        MenuItem rssItem = new MenuItem(title);
        
        // If the News item is clicked 
        rssItem.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                try {
                    Desktop.getDesktop().browse(url);
                    } catch (Exception ex) {
                        logger.severe("Could not navigate to news web-page");
                    }   
            }
        });
        return rssItem;
    }

    public static MenuItem createExitMenuItem()
    {
        MenuItem exitItem = new MenuItem("Exit");
        
        // If the News item is clicked 
        exitItem.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                try {
                    System.exit(0);
                    } catch (Exception ex) {
                        logger.severe("Could not quit program " +ex.getMessage());
                    }   
            }
        });
        return exitItem;
    }
    
    public static void create(Outline outline, Menu menu)
    {
        if (outline.getOutline()!=null) {logger.fine("This one has outlines");}
        
        
        // See if this one has children of type outline, add and handle them recursively
        if (outline.getOutline()!=null && !outline.getOutline().isEmpty())
        {
            for (Outline childOutline : outline.getOutline())
            {
                Menu submenu = new Menu(childOutline.getText());
                menu.add(submenu);
                submenu.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Call the update method below to remove old items and load new ones
                        Menu menu = (Menu)e.getSource();
                        update(childOutline.getXmlUrl(), menu);
                    }
                });
                create(childOutline, submenu);
            }
        }
        else // If it has a feed URL, update the feed
        if (outline.getXmlUrl() != null && !outline.getXmlUrl().isEmpty())
        {
            update(outline.getXmlUrl(), menu);
        }
    }

}
