package io.github.clouderhem.jvmtools.agentmain.strategy.impl;

import io.github.clouderhem.jvmtools.agentmain.strategy.CmdProcessStrategy;
import io.github.clouderhem.jvmtools.agentmain.transformer.ClassHotSwapTransformer;
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
 * @date 8/18/2023 9:13 PM
 */
public class ClassHotSwapStrategyImpl implements CmdProcessStrategy {

    private static final Logger log = LoggerFactory.getLogger(ClassHotSwapStrategyImpl.class);

    public static final String APPLY_OP = "hotswapclass";

    @Override
    public boolean canApply(String args) {
        List<String> argList =
                Arrays.stream(args.split(" ")).filter(StringUtils::isNotBlank).map(String::trim).collect(Collectors.toList());

        return argList.size() == 3 && APPLY_OP.equals(argList.get(0));
    }

    @Override
    public void process(String args, Instrumentation instrumentation) {
        String[] argList = args.split(" ");
        String className = argList[1];
        String clazzFileUrl = argList[2];

        ClassHotSwapTransformer classHotSwapTransformer =
                new ClassHotSwapTransformer(className, clazzFileUrl);

        try {
            instrumentation.addTransformer(classHotSwapTransformer, true);
            instrumentation.retransformClasses(Class.forName(className));
        } catch (UnmodifiableClassException | ClassNotFoundException e) {
            log.error("", e);
        } finally {
            instrumentation.removeTransformer(classHotSwapTransformer);
        }
    }
}
