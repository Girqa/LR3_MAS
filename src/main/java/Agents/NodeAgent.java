package Agents;

import AdditionalClasses.NodeCfg;
import AdditionalClasses.ParsingProvider;
import Behaviours.BackPropagationBehaviour;
import Behaviours.InitiatorBehaviour;
import Behaviours.InitiatorFMSBehaviour;
import Behaviours.RequestProcessingBehaviour;
import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class NodeAgent extends Agent {
    @Override
    protected void setup() {
        File source = new File("src/main/resources/" + getLocalName().toLowerCase() + ".xml");
        NodeCfg cfg = ParsingProvider.unmarshal(source, NodeCfg.class);
        log.info("Created agent {} with config {}", getLocalName(), cfg);

        if (cfg.isInitiator()) {
            addBehaviour(new WakerBehaviour(this, 12000L) {
                @Override
                protected void onWake() {
                    getAgent().addBehaviour(new InitiatorFMSBehaviour(cfg));
                    //getAgent().addBehaviour(new InitiatorBehaviour(cfg));
                }
            });
           // addBehaviour(new InitiatorBehaviour(cfg));
        } else {
            addBehaviour(new RequestProcessingBehaviour(cfg));
        }
    }
}