package AdditionalClasses;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Link {
    @XmlElement(name="neighbour")
    private String neighbourAgent;
    @XmlElement(name="weight")
    private int weight;
}
