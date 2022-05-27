package org.hemmerit.riss.datamodel.opml;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Outline implements Serializable {
    
    @XmlAttribute
    public String type;
    
    @XmlAttribute
    public String title;
    
    @XmlAttribute
    public String text;
    
    @XmlAttribute
    public String xmlUrl;
    
    @XmlElement
    private List<Outline> outline;

    public List<Outline> getOutline() {return outline;}
    public void setOutline(List<Outline> outline) {this.outline = outline;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getText() {return text;}
    public void setText(String text) {this.text = text;}

    public String getXmlUrl() {return xmlUrl;}
    public void setXmlUrl(String xmlUrl) {this.xmlUrl = xmlUrl;}
    
    public String getType() {return type;}
    public void setType(String type) {this.type = type;}
}
