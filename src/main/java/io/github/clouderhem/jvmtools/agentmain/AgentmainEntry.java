package io.github.clouderhem.jvmtools.agentmain;

import io.github.clouderhem.jvmtools.net.starter.ServerBootstrapStarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;

/**
 * @author Aaron Yeung
 * @date 8/17/2023 1:31 PM
 */
public class AgentmainEntry {

    private static final Logger log = LoggerFactory.getLogger(AgentmainEntry.class);

    public static void agentmain(String args, Instrumentation inst) {
        log.info("Agentmain invoked, args:{}", args);

        new ServerBootstrapStarter(inst).start();
    }
}

