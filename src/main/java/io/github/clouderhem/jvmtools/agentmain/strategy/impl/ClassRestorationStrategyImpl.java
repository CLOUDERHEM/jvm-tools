package io.github.clouderhem.jvmtools.agentmain.strategy.impl;

import com.google.common.collect.Sets;
import io.github.clouderhem.jvmtools.agentmain.strategy.CmdProcessStrategy;
import io.github.clouderhem.jvmtools.common.util.ArgsUtils;
import io.github.clouderhem.jvmtools.common.util.InstrumentationUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;
import java.util.List;

/**
 * @author Aaron Yeung
 * @date 8/18/2023 3:05 PM
 */
public class ClassRestorationStrategyImpl implements CmdProcessStrategy {

    private static final Logger log = LoggerFactory.getLogger(ClassRestorationStrategyImpl.class);

    public static final String APPLY_OP = "restoreclass";

    @Override
    public boolean canApply(String args) {
        List<String> argList = ArgsUtils.buildArgList(args, StringUtils.SPACE);
        return argList.size() == 2 && APPLY_OP.equals(argList.get(0).trim());
    }

    @Override
    public void process(String args, Instrumentation instrumentation) {
        List<String> argList = ArgsUtils.buildArgList(args, StringUtils.SPACE);
        String className = argList.get(1);

        // 此时没有transformer, 默认生成原有的字节码
        InstrumentationUtils.retransformClasses(instrumentation, Sets.newHashSet(className));

    }
}
