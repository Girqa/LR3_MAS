package AdditionalClasses;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="cfg")
public class NodeCfg {
    @XmlElement
    private String name;
    @XmlElement
    private boolean initiator;
    @XmlElementWrapper(name="links")
    @XmlElement(name="link")
    private Link[] links;

}
