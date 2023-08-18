package io.github.clouderhem.jvmtools.agentmain.strategy.impl;

import io.github.clouderhem.jvmtools.agentmain.strategy.CmdProcessStrategy;
import io.github.clouderhem.jvmtools.agentmain.transformer.ClassRestorationTransformer;
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
 * @date 8/18/2023 3:05 PM
 */
public class ClassRestorationStrategyImpl implements CmdProcessStrategy {

    private static final Logger log = LoggerFactory.getLogger(ClassRestorationStrategyImpl.class);

    public static final String APPLY_OP = "restoreclass";

    @Override
    public boolean canApply(String args) {
        List<String> argList =
                Arrays.stream(args.split(" ")).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        return argList.size() == 2 && APPLY_OP.equals(argList.get(0).trim());
    }

    @Override
    public void process(String args, Instrumentation instrumentation) {

        String[] argList = args.split(" ");

        ClassRestorationTransformer classRestorationTransformer = new ClassRestorationTransformer();
        try {
            instrumentation.addTransformer(classRestorationTransformer, true);
            // 还原修改过的class
            instrumentation.retransformClasses(Class.forName(argList[1].trim()));
        } catch (UnmodifiableClassException | ClassNotFoundException e) {
            log.error("", e);
        } finally {
            instrumentation.removeTransformer(classRestorationTransformer);
        }
    }
}