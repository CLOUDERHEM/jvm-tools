package io.github.clouderhem.jvmtools.agentmain;

import io.github.clouderhem.jvmtools.agentmain.strategy.AgentMainStrategy;
import io.github.clouderhem.jvmtools.agentmain.strategy.AgentMainStrategyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;
import java.util.Optional;

/**
 * @author Aaron Yeung
 * @date 8/17/2023 1:31 PM
 */
public class AgentmainEntry {

    private static final Logger log = LoggerFactory.getLogger(AgentmainEntry.class);

    public static void agentmain(String args, Instrumentation inst) {
        log.info("Invoke agent main, args:{}", args);

        Optional<AgentMainStrategy> strategy = AgentMainStrategyFactory.findStrategy(args);

        if (!strategy.isPresent()) {
            log.error("{} is not support", args);
            return;
        }

        strategy.get().process(args, inst);
    }
}
