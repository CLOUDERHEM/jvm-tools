package io.github.clouderhem.jvmtools.premain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;

/**
 * @author Aaron Yeung
 * @date 8/17/2023 1:20 PM
 */
@Deprecated
public class PremainEntry {
    private static final Logger log = LoggerFactory.getLogger(PremainEntry.class);

    public static void premain(String args, Instrumentation inst) {
        log.info("invoke premain, arg:{}", args);
    }
}
