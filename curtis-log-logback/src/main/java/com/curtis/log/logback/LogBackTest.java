package com.curtis.log.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogBackTest {

    public static final Logger LOGGER = LoggerFactory.getLogger(LogBackTest.class);
    public static final Logger LOGGER_OBJ = LoggerFactory.getLogger(Object.class);

    public static void main(String[] args) {
        LOGGER.info("this is a message");
        LOGGER_OBJ.info("this is a message");
    }
}
