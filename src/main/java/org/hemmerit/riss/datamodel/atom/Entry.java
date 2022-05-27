package org.hemmerit.riss.datamodel.atom;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "entry", namespace="http://www.w3.org/2005/Atom")
public class Entry implements Serializable {
    
    @XmlElement(name="title", namespace="http://www.w3.org/2005/Atom")
    public String title;
    
    @XmlElement(name="link", namespace="http://www.w3.org/2005/Atom")
    public Link link;

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public Link getLink() {return link;}
    public void setLink(Link link) {this.link = link;}
}
