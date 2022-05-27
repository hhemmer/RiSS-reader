package org.hemmerit.riss.datamodel.rss;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item")
public class Item implements Serializable {
    
    public String title;
    public String link;
    public String description;

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getLink() {return link;}
    public void setLink(String link) {this.link = link;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
}
