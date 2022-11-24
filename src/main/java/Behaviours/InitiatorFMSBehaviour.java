package Behaviours;

import AdditionalClasses.MsgContent;
import AdditionalClasses.NodeCfg;
import jade.core.behaviours.FSMBehaviour;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InitiatorFMSBehaviour extends FSMBehaviour {
    private final String INIT = "INIT", COLLECT = "COLLECT", PROCESS = "PROCESS";
    public InitiatorFMSBehaviour(NodeCfg cfg) {

        List<MsgContent> answers = new ArrayList<>();

        registerFirstState(new InitiatorBehaviour(cfg), INIT);
        registerState(new InitiatorParallelBehaviour(getAgent(), answers), COLLECT);
        registerLastState(new ResultsProcessingBehaviour(answers), PROCESS);

        registerDefaultTransition(INIT, COLLECT);
        registerDefaultTransition(COLLECT, PROCESS);
    }
}
