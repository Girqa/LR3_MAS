package AdditionalClasses;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="cfg")
public class NodeCfg {
    @XmlElement
    private String name;
    @XmlElement
    private boolean initiator;
    @XmlElement
    private String lookForAgent;
    @XmlElementWrapper(name="links")
    @XmlElement(name="link")
    private List<Link> links;
}
