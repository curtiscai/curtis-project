package com.curtis.log.logback.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class CustomerAppender extends AppenderBase<ILoggingEvent> {

    @Override
    protected void append(ILoggingEvent iLoggingEvent) {
        String loggerName = iLoggingEvent.getLoggerName();
        System.out.println(loggerName);
    }
}
