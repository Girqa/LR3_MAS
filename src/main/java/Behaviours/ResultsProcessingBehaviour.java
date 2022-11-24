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
        int allWays = answers.size();
        List<MsgContent> deadLocs = answers.stream().filter(msg -> msg.isDeadlock()).collect(Collectors.toList());
        int allDeadLocs = deadLocs.size();
        answers.removeAll(deadLocs);
        List<MsgContent> rightWays = answers;
        int allRightWays = rightWays.size();
        Optional<MsgContent> bestWay = bestWay(rightWays);

        log.info("\nТупиковые маршруты: \n{}", deadLocs.size() == 0 ? "отсутствуют": joinList(deadLocs));
        log.info("\nЗавершенные маршруты: \n{}", rightWays.size() == 0 ? "отсутствуют": joinList(rightWays));
        log.info("\nЛучший маршрут: \n{}", bestWay.isPresent() ? presentMsgContent(bestWay.get()) : "отсутствует");
        log.info("\nОбщее число построенных маршрутов {}", allWays);
        log.info("\nОбщее число тупиковых маршрутов {}", allDeadLocs);
        log.info("\nОбщее число завершенных маршрутов {}", allRightWays);
    }

    private Optional<MsgContent> bestWay(List<MsgContent> rightWays) {
        Optional<MsgContent> best = rightWays.stream().sorted(
                (o1, o2) -> {
            if (o1.getWeight() > o2.getWeight()) {
                return 1;
            } else if (o1.getWeight() == o2.getWeight()) {
                return 0;
            } else {
                return -1;
            }
        })
                .findFirst();
        return best;
    }
    private String presentMsgContent(MsgContent content) {
        return "\tpassed nodes: " + content.getPassedNodes().toString() +
                " full weight: " + content.getWeight();
    }

    /**
     * Можно и лутчше, но так понятнее
     */
    private String joinList(List<MsgContent> list) {
        List<String> ways = new ArrayList<>();
        list.forEach(way -> ways.add(presentMsgContent(way)));
        return ways.stream().collect(Collectors.joining("\n"));
    }
}
