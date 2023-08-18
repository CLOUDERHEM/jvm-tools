package io.github.clouderhem.jvmtools.agentmain.strategy.impl;

import io.github.clouderhem.jvmtools.agentmain.strategy.CmdProcessStrategy;
import io.github.clouderhem.jvmtools.net.NetServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;

/**
 * @author Aaron Yeung
 * @date 8/18/2023 5:05 PM
 */
public class ShutdownStrategyImpl implements CmdProcessStrategy {

    private static final Logger log = LoggerFactory.getLogger(ShutdownStrategyImpl.class);

    public static final String APPLY_OP = "shutdown";

    @Override
    public boolean canApply(String args) {
        return APPLY_OP.equals(args.trim());
    }

    @Override
    public void process(String args, Instrumentation instrumentation) {
        log.info("Server is about to be shut down");
        NetServer.shutdownServer();
    }
}
