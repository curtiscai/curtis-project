package com.curtis.log.log4j2;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;

public class MyApp {
    public static void main(String[] args) {
        Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
        logger.trace("trace level");
        logger.debug("debug level");
        logger.info("info level");
        logger.warn("warn level");
        logger.error("error level");
        logger.fatal("fatal level");

        org.slf4j.Logger logger2 = LoggerFactory.getLogger("");
        logger2.trace("trace level");
        logger2.debug("debug level");
        logger2.info("info level");
        logger2.warn("warn level");
        logger2.error("error level");
        logger.fatal("fatal level");
    }
}
