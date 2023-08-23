package io.github.clouderhem.jvmtools.agentmain.strategy.impl;

import com.google.common.collect.Sets;
import io.github.clouderhem.jvmtools.agentmain.strategy.CmdProcessStrategy;
import io.github.clouderhem.jvmtools.agentmain.transformer.ClassHotSwapTransformer;
import io.github.clouderhem.jvmtools.common.util.ArgsUtils;
import io.github.clouderhem.jvmtools.common.util.InstrumentationUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;
import java.util.List;

/**
 * @author Aaron Yeung
 * @date 8/18/2023 9:13 PM
 */
public class ClassHotSwapStrategyImpl implements CmdProcessStrategy {

    private static final Logger log = LoggerFactory.getLogger(ClassHotSwapStrategyImpl.class);

    public static final String APPLY_OP = "hotswapclass";

    @Override
    public boolean canApply(String args) {
        List<String> argList = ArgsUtils.buildArgList(args, StringUtils.SPACE);
        return argList.size() == 3 && APPLY_OP.equals(argList.get(0));
    }

    @Override
    public void process(String args, Instrumentation instrumentation) {
        List<String> argList = ArgsUtils.buildArgList(args, StringUtils.SPACE);
        String className = argList.get(1);
        String clazzFileUrl = argList.get(2);

        ClassHotSwapTransformer classHotSwapTransformer =
                new ClassHotSwapTransformer(className, clazzFileUrl);

        try {
            instrumentation.addTransformer(classHotSwapTransformer, true);
            InstrumentationUtils.retransformClasses(instrumentation, Sets.newHashSet(className));
        } finally {
            instrumentation.removeTransformer(classHotSwapTransformer);
        }
    }
}
