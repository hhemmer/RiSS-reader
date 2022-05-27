package org.hemmerit.riss.datamodel.atom;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "feed", namespace="http://www.w3.org/2005/Atom")
public class Feed implements Serializable {
    
    @XmlElement(name="entry", namespace="http://www.w3.org/2005/Atom")
    private List<Entry> entry;

    public List<Entry> getEntry() {
        return entry;
    }

    public void setEntry(List<Entry> entry) {
        this.entry = entry;
    }
    
    
	
    
}
