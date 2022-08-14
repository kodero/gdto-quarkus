package com.corvid.genericdto.util;

import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;


public class LoggingUtil {
    
    private static Logger log = Logger.getLogger(LoggingUtil.class.getName());

    public static void log(Class<?> logSource, Level level, String msg){
        if (log.isEnabled(level)) {
            log.log(level, String.format("[LogSource= %s]: %s", logSource.getName(), msg));
        }
    }

    private LoggingUtil(){}
}
