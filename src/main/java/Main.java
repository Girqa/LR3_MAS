import Agents.NodeAgent;
import AgentsFactory.AgentDescription;
import AgentsFactory.AgentFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class Main {
    public static void main(String[] args) {
        String testPath = "TestFromBook/";

        File configs = new File("src/main/resources/"+testPath);
        NodeAgent.path = configs.getPath()+"/";
        File[] files = configs.listFiles(pathname -> pathname.getName().contains("node"));
        List<AgentDescription> dss = new LinkedList<>();

        assert files != null;
        for (File file: files) {
            String name = file.getName().replace(".xml", "");
            name = capitalize(name);
            dss.add(new AgentDescription(name, NodeAgent.class));
        }
        AgentFactory.createAgents(dss);
    }

    private static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
