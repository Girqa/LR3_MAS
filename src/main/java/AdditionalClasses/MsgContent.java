package AdditionalClasses;

import lombok.Data;

import java.util.List;

@Data
public class MsgContent {
    private String initiator;
    private String lookForAgent;
    private int weight;
    private boolean deadlock;
    private List<String> passedNodes;
    public void increaseWeight(int addWeight) {
        this.weight += addWeight;
    }
}
