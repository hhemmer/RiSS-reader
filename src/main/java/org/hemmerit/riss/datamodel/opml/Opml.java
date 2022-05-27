package org.hemmerit.riss.datamodel.opml;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "opml")
public class Opml implements Serializable {
    
    public Head head;
    public Body body;
}
