package io.github.clouderhem.jvmtools.agentmain.strategy.impl;

import io.github.clouderhem.jvmtools.agentmain.strategy.CmdProcessStrategy;
import io.github.clouderhem.jvmtools.agentmain.transformer.MethodParameterLogTransformer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Aaron Yeung
 * @date 8/17/2023 3:12 PM
 */
public class MethodParameterLogStrategyImpl implements CmdProcessStrategy {

    public static final String APPLY_OP = "methodlog";

    private static final Logger log = LoggerFactory.getLogger(MethodParameterLogStrategyImpl.class);


    @Override
    public boolean canApply(String args) {
        List<String> argList =
                Arrays.stream(args.split(" ")).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        return argList.size() == 3 && APPLY_OP.equals(argList.get(0).trim());
    }

    @Override
    public void process(String args, Instrumentation instrumentation) {
        String[] argList = args.split(" ");
        MethodParameterLogTransformer methodParameterLogTransformer =
                new MethodParameterLogTransformer(argList[1].trim(), argList[2].trim());

        try {
            instrumentation.addTransformer(methodParameterLogTransformer, true);
            instrumentation.retransformClasses(Class.forName(argList[1].trim()));
        } catch (ClassNotFoundException | UnmodifiableClassException e) {
            log.error("", e);
        }
    }
}

