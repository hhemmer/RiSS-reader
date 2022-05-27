package org.hemmerit.riss.datamodel.opml;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "body")
public class Body implements Serializable {
    
    private List<Outline> outline;

    public List<Outline> getOutline() {return outline;}
    public void setOutline(List<Outline> outline) {this.outline = outline;}
}
