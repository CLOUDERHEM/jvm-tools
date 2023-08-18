package io.github.clouderhem.jvmtools.agentmain.strategy;

import java.lang.instrument.Instrumentation;

/**
 * @author Aaron Yeung
 * @date 8/17/2023 3:04 PM
 */
public interface CmdProcessStrategy {

    boolean canApply(String args);

    void process(String args, Instrumentation instrumentation);
}
