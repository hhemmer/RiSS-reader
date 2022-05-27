# RiSS
## Read it Simple, Stupid!
RSS feeds are a wonderful and pure way of receiving a list of recent articles available on websites supporting the standard.
RiSS is a minimal tray icon based application that let you check the latest updates on websites without any overhead.
It is designed in a similar way as the RSS _Live Bookmarks_ feature available in Firefox for many years, but that is now [deprecated](https://support.mozilla.org/en-US/kb/live-bookmarks-migration).
RiSS is targeted to fill that gap. 
## Requirements
As a Java application, RiSS needs a _Java Runtime Environment (JRE)_ installed, available for example as [AdoptOpenJDK](https://adoptopenjdk.net/) version.
## Download
Just click the Code button at the right corner on top of the directory view above and select the **Download ZIP** option.
## Build
RiSS uses the [Apache Maven](https://maven.apache.org/) build system. Therefore it is easy to build when Maven is installed. Just type the following command in the directory where you unpacked the RiSS ZIP into:
> mvn install

RiSS is an old application, built against the JDK 8 version, JAXB classes are removed later, which is currently not taken into account. Please ensure to use a JDK 8 for building and launching RiSS, e.g. by explicitely configuring the _JDK_HOME_ variable:
> export JAVA_HOME=/Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home            
## Configure
When started, the app is creating a RiSS folder in the user directory (e.g _/Users/username/RiSS_ on a Mac or _C:\Users\username\RiSS_) that holds a **feeds.opml** file and the logs of the application.
Feel free to copy in your old Firefox RSS _backup.opml_ file here (unfortunately, the exported files do not include any hierachy as in the example bundled with this app, but you can nest the XML tags and RiSS will create hierachical menus).
## Launch
The easiest way to launch the application is to start the software by entering the following command in a terminal of choice in the directory you have built it in:
> java -jar target/riss-RSS-reader.jar
## Use
After launching, RiSS is showing a menu item in your menu bar. A left click on it displays the (hierchical) menu of RSS feeds and when pointing the mouse on them, the news items. Left click on an item shows the news in your standard browser:
![RiSS in action](riss.gif "RiSS in action")<!-- .element height="50%" width="50%" -->
## Dev Notes
RiSS is using [JAXB](https://javaee.github.io/jaxb-v2/) to transform the XML data received from the RSS services to Java Objects and a simple recursion to create the menus.
