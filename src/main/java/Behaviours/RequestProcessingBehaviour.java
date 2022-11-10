package Behaviours;

import AdditionalClasses.Link;
import AdditionalClasses.MsgContent;
import AdditionalClasses.NodeCfg;
import AdditionalClasses.ParsingProvider;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
public class RequestProcessingBehaviour extends Behaviour {
    private NodeCfg cfg;
    private MessageTemplate mt;
    public RequestProcessingBehaviour(NodeCfg cfg) {
        this.cfg = cfg;
    }

    @Override
    public void onStart() {
        mt = MessageTemplate.and(
                MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                MessageTemplate.MatchProtocol("processing")
        );
    }

    @Override
    public void action() {
        ACLMessage msg = getAgent().receive(mt);
        if (msg != null) {
            log.info("{} received {}", getAgent().getLocalName(), msg.getContent());
            MsgContent content = processMsg(msg, cfg);
            // Отсеяли все пройденные узлы
            List<Link> links = cfg.getLinks()
                    .stream()
                    .filter(link -> !content.getPassedNodes().contains(link.getNeighbourAgent()))
                    .collect(Collectors.toList());
            ACLMessage request = new ACLMessage(ACLMessage.INFORM);
            if (!getAgent().getLocalName().equals(content.getLookForAgent()) && links.size() != 0) {
                // Перенаправили информацию о пути соседям
                request.setProtocol("processing");
                String jsonContent = ParsingProvider.toJson(content);
                request.setContent(jsonContent);
                sendToNeighbors(request, links);
                log.info("{} sends {} to {}", getAgent().getLocalName(), content, links);
            } else if (links.size() == 0 && !getAgent().getLocalName().equals(content.getLookForAgent())){
                // Отправили результат инициатору с уточнением, что зашли в тупик
                content.setDeadlock(true);
                sendToInitiator(request, content);
                log.info("{} sends {} to Initiator", getAgent().getLocalName(), content);
            } else {
                // Отправили результат инициатору
                sendToInitiator(request, content);
                log.info("{} sends {} to Initiator", getAgent().getLocalName(), content);
            }
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }

    private int findSenderWight(List<Link> links, String sender) {
        for (Link link: links) {
            if (link.getNeighbourAgent().equals(sender)) {
                return link.getWeight();
            }
        }
        throw new NoSuchElementException("Неверная конфигурация узла. Узел должен содержать информацию о всех соседях");
    }

    private void sendToNeighbors(ACLMessage request, List<Link> links) {
        links.forEach(link -> request.addReceiver(new AID(link.getNeighbourAgent(), false)));
        getAgent().send(request);
    }

    private void sendToInitiator(ACLMessage request, MsgContent content) {
        request.addReceiver(new AID(content.getInitiator(),false));
        request.setProtocol("finished");
        String jsonContent = ParsingProvider.toJson(content);
        request.setContent(jsonContent);
        request.setPerformative(ACLMessage.PROPOSE);
        getAgent().send(request);
    }

    private MsgContent processMsg(ACLMessage msg, NodeCfg cfg) {
        MsgContent content = ParsingProvider.fromJson(msg.getContent(), MsgContent.class);
        List<Link> links = cfg.getLinks();
        // Учли пройденный от отправителя путь
        int weightFromSender = findSenderWight(links, msg.getSender().getLocalName());
        content.increaseWeight(weightFromSender);
        // Включили себя в пройденный путь
        content.getPassedNodes().add(cfg.getName());
        return content;
    }
}
