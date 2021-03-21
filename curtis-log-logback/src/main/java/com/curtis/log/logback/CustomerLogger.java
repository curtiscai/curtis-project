// package com.curtis.logback;
//
// import ch.qos.logback.classic.LoggerContext;
// import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
// import ch.qos.logback.core.rolling.RollingFileAppender;
// import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP;
// import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
//
// public class CustomerLogger {
//
//     public static Logger getLogger(String jobName, Class<?> cls) {
//         Logger logger = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(cls);
//
//         LoggerContext loggerContext = logger.getLoggerContext();
//         PatternLayoutEncoder encoder = new PatternLayoutEncoder();
//         encoder.setContext(loggerContext);
//         encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n");
//         encoder.start();
//
//         RollingFileAppender appender = new RollingFileAppender();
//         appender.setContext(loggerContext);
//         TimeBasedRollingPolicy rollingPolicyBase = new TimeBasedRollingPolicy<>();
//         rollingPolicyBase.setContext(loggerContext);
//         rollingPolicyBase.setParent(appender);
//         rollingPolicyBase.setFileNamePattern((String.format("%s/job-schedule/mesp-schedule-%s", logFilePath, jobName) + ".%d{yyyy-MM-dd}.%i.log"));
//         SizeAndTimeBasedFNATP sizeAndTimeBasedFNATP = new SizeAndTimeBasedFNATP();
//         sizeAndTimeBasedFNATP.setMaxFileSize("10MB");
//         rollingPolicyBase.setTimeBasedFileNamingAndTriggeringPolicy(sizeAndTimeBasedFNATP);
//         rollingPolicyBase.setMaxHistory(10);
//         rollingPolicyBase.start();
//
//         appender.setEncoder(encoder);
//         appender.setRollingPolicy(rollingPolicyBase);
//         appender.start();
//
//         logger.setAdditive(false);
//         logger.addAppender(appender);
//
//         return logger;
//     }
// }
