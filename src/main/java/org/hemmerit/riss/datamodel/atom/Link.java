package org.hemmerit.riss.datamodel.atom;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "link", namespace="http://www.w3.org/2005/Atom")
public class Link implements Serializable {
    
    @XmlAttribute
    public String href;

    public String getHref() {return href;}
    public void setHref(String href) {this.href = href;}
}
