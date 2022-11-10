package Behaviours;

import AdditionalClasses.MsgContent;
import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.WakerBehaviour;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class InitiatorParallelBehaviour extends ParallelBehaviour {

    public InitiatorParallelBehaviour(Agent a, List<MsgContent> answers) {
        super(a, WHEN_ANY);
        log.debug("Receiving answers started");
        addSubBehaviour(new WakerBehaviour(getAgent(), 1000L) {});
        addSubBehaviour(new BackPropagationBehaviour(answers));
    }

    @Override
    public int onEnd() {
        log.debug("Receiving answers ended");
        return 1;
    }
}
