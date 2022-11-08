package org.example;

import AdditionalClasses.NodeCfg;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;

@Slf4j
public class Main {
    @SneakyThrows
    public static void main(String[] args) {
        log.info("Unmarshalling Node");
        File source = new File("src/main/resources/node1.xml");
        JAXBContext context = JAXBContext.newInstance(NodeCfg.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        log.info(String.valueOf((NodeCfg) unmarshaller.unmarshal(source)));
    }
}