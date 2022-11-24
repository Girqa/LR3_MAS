package Behaviours;

import AdditionalClasses.MsgContent;
import AdditionalClasses.NodeCfg;
import AdditionalClasses.ParsingProvider;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class BackPropagationBehaviour extends Behaviour {
    private List<MsgContent> answers;
    private MessageTemplate mt;
    public BackPropagationBehaviour(List<MsgContent> answers) {
        this.answers = answers;
    }

    @Override
    public void onStart() {
        mt = MessageTemplate.and(
                MessageTemplate.MatchPerformative(ACLMessage.PROPOSE),
                MessageTemplate.MatchProtocol("finished")
        );
    }

    @Override
    public void action() {
        ACLMessage msg = getAgent().receive(mt);
        if (msg != null) {
            log.info("Initiator got {} from {}", msg.getContent(), msg.getSender().getLocalName());
            answers.add(ParsingProvider.fromJson(msg.getContent(), MsgContent.class));
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
