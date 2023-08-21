package io.github.clouderhem.jvmtools.agentmain.strategy.impl;

import io.github.clouderhem.jvmtools.agentmain.strategy.CmdProcessStrategy;
import io.github.clouderhem.jvmtools.agentmain.transformer.MethodParameterLogTransformer;
import io.github.clouderhem.jvmtools.common.util.ArgsUtils;
import io.github.clouderhem.jvmtools.common.util.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.List;


/**
 * @author Aaron Yeung
 * @date 8/17/2023 3:12 PM
 */
public class MethodParameterLogStrategyImpl implements CmdProcessStrategy {

    public static final String APPLY_OP = "methodlog";

    private static final Logger log = LoggerFactory.getLogger(MethodParameterLogStrategyImpl.class);


    @Override
    public boolean canApply(String args) {
        List<String> argList = ArgsUtils.buildArgList(args, StringUtils.SPACE);
        return argList.size() == 3 && APPLY_OP.equals(argList.get(0).trim());
    }

    @Override
    public void process(String args, Instrumentation instrumentation) {
        List<String> argList = ArgsUtils.buildArgList(args, StringUtils.SPACE);
        String className = argList.get(1);
        String methodName = argList.get(2);

        MethodParameterLogTransformer methodParameterLogTransformer =
                new MethodParameterLogTransformer(className, methodName);

        try {
            instrumentation.addTransformer(methodParameterLogTransformer, true);
            instrumentation.retransformClasses(ClassUtils.findClass(className));
        } catch (ClassNotFoundException | UnmodifiableClassException e) {
            log.error("", e);
        } finally {
            instrumentation.removeTransformer(methodParameterLogTransformer);
        }
    }
}

