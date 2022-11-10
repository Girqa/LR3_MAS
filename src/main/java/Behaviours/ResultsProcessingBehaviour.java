package Behaviours;

import AdditionalClasses.MsgContent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class ResultsProcessingBehaviour extends OneShotBehaviour {
    private List<MsgContent> answers;
    public ResultsProcessingBehaviour(List<MsgContent> answers) {
        this.answers = answers;
    }

    @Override
    public void action() {
        List<MsgContent> deadLocs = answers.stream().filter(msg -> msg.isDeadlock()).collect(Collectors.toList());
        answers.removeAll(deadLocs);
        List<MsgContent> rightWays = answers;
        Optional<MsgContent> bestWay = bestWay(rightWays);

        log.info("\nТупиковые маршруты: \n{}", deadLocs.size() == 0 ? "отсутствуют": joinList(deadLocs));
        log.info("\nЗавершенные маршруты: \n{}", rightWays.size() == 0 ? "отсутствуют": joinList(rightWays));
        log.info("\nЛучший маршрут: \n{}", bestWay.isPresent() ? presentMsgContent(bestWay.get()) : "отсутствует");
    }

    private Optional<MsgContent> bestWay(List<MsgContent> rightWays) {
        Optional<MsgContent> best = rightWays.stream().sorted(new Comparator<MsgContent>() {
            @Override
            public int compare(MsgContent o1, MsgContent o2) {
                if (o1.getWeight() > o2.getWeight()) {
                    return 1;
                } else if (o1.getWeight() == o2.getWeight()) {
                    return 0;
                } else {
                    return -1;
                }
            }
        }).findFirst();
        return best;
    }
    private String presentMsgContent(MsgContent content) {
        return "    passed nodes: " + content.getPassedNodes().toString() + " full weight: " + content.getWeight();
    }

    private String joinList(List<MsgContent> list) {
        List<String> ways = new ArrayList<>();
        list.forEach(way -> ways.add(presentMsgContent(way)));
        return ways.stream().collect(Collectors.joining("\n"));
    }
}