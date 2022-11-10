package Behaviours;

import AdditionalClasses.Link;
import AdditionalClasses.MsgContent;
import AdditionalClasses.NodeCfg;
import AdditionalClasses.ParsingProvider;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

@Slf4j
public class InitiatorBehaviour extends OneShotBehaviour {
    private NodeCfg cfg;
    public InitiatorBehaviour(NodeCfg cfg) {
        log.info("Initiator started");
        this.cfg = cfg;
    }

    @Override
    public void action() {
        // Заполняем поля сообщения
        MsgContent content = new MsgContent();
        content.setInitiator(cfg.getName());
        content.setLookForAgent(cfg.getLookForAgent());
        content.setPassedNodes(List.of(cfg.getName()));
        // Собрали всех соседей и добавили в получателей запроса
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        List<Link> links = cfg.getLinks();
        links.forEach((link) -> msg.addReceiver(new AID(link.getNeighbourAgent(), false)));
        // Шлем сообщение инициатора соседям
        String jsonContent = ParsingProvider.toJson(content);
        log.info("Initiator sends {} to {}", jsonContent, links.stream()
                .map(Link::getNeighbourAgent)
                .collect(Collectors.toList()));
        msg.setContent(jsonContent);
        msg.setProtocol("processing");
        getAgent().send(msg);
    }
}
