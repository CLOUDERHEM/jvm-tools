package io.github.clouderhem.jvmtools.common.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Aaron Yeung
 * @date 9/1/2023 2:52 PM
 */
public class LogEntry {

    private static final Logger logger = LoggerFactory.getLogger(LogEntry.class);

    public static void info(String log) {
        // todo netty support
        logger.info(log);
    }
}
