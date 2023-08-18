package io.github.clouderhem.jvmtools.agentmain.strategy;

import io.github.clouderhem.jvmtools.agentmain.strategy.impl.ClassRestorationStrategyImpl;
import io.github.clouderhem.jvmtools.agentmain.strategy.impl.MethodParameterLogStrategyImpl;
import io.github.clouderhem.jvmtools.agentmain.strategy.impl.ShutdownStrategyImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Aaron Yeung
 * @date 8/17/2023 3:15 PM
 */
public class CmdProcessStrategyFactory {

    public static final List<CmdProcessStrategy> AGENT_MAIN_STRATEGY_LIST = new ArrayList<>();

    static {
        AGENT_MAIN_STRATEGY_LIST.add(new ClassRestorationStrategyImpl());
        AGENT_MAIN_STRATEGY_LIST.add(new MethodParameterLogStrategyImpl());
        AGENT_MAIN_STRATEGY_LIST.add(new ShutdownStrategyImpl());
    }

    public static Optional<CmdProcessStrategy> findStrategy(String args) {
        return AGENT_MAIN_STRATEGY_LIST.stream()
                .filter(cmdProcessStrategy -> cmdProcessStrategy.canApply(args)).findAny();
    }
}
