package io.github.clouderhem.jvmtools.agentmain.strategy;

import io.github.clouderhem.jvmtools.agentmain.strategy.impl.MethodParameterLogStrategyImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Aaron Yeung
 * @date 8/17/2023 3:15 PM
 */
public class AgentMainStrategyFactory {

    private static final List<AgentMainStrategy> AGENT_MAIN_STRATEGY_LIST = new ArrayList<>();

    static {
        AGENT_MAIN_STRATEGY_LIST.add(new MethodParameterLogStrategyImpl());
    }

    public static Optional<AgentMainStrategy> findStrategy(String args) {
        return AGENT_MAIN_STRATEGY_LIST.stream()
                .filter(agentMainStrategy -> agentMainStrategy.canApply(args)).findAny();
    }
}
