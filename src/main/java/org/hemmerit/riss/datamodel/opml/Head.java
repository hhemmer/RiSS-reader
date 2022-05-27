package org.hemmerit.riss.datamodel.opml;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "head")
public class Head implements Serializable {
    
    @XmlAttribute
    public String title;

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
}
