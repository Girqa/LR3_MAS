package org.example;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class Main {
    public static void main(String[] args) {
        log.debug("Debugging {}", new Date());
        log.trace("Spam info {}", new Date());
        log.error("ERROR!!! {}", new Date());
        log.warn("WARNING {}", new Date());
    }
}