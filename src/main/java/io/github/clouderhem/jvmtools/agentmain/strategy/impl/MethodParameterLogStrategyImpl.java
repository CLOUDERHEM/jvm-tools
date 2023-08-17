package io.github.clouderhem.jvmtools.agentmain.strategy.impl;

import com.google.common.collect.Lists;
import io.github.clouderhem.jvmtools.agentmain.strategy.AgentMainStrategy;
import javassist.*;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Aaron Yeung
 * @date 8/17/2023 3:12 PM
 */
public class MethodParameterLogStrategyImpl implements AgentMainStrategy {

    private static final Logger log = LoggerFactory.getLogger(MethodParameterLogStrategyImpl.class);

    private static final String APPLY_OP = "method";

    @Override
    public boolean canApply(String args) {
        List<String> argList =
                Arrays.stream(args.split(" ")).filter(StringUtils::isNotBlank).collect(Collectors.toList());

        return argList.size() == 4 && APPLY_OP.equals(argList.get(0).trim());
    }

    @Override
    public void process(String args, Instrumentation instrumentation) {
        String[] argList = args.split(" ");
        MethodParameterLogTransformer transformer =
                new MethodParameterLogTransformer(argList[1].trim(), argList[2].trim());

        try {
            instrumentation.addTransformer(transformer, true);
            instrumentation.retransformClasses(Class.forName(argList[1].trim()));
        } catch (ClassNotFoundException | UnmodifiableClassException e) {
            log.error("", e);
        }

        try {
            Thread.sleep(Integer.parseInt(argList[3]) * 1000L);
        } catch (InterruptedException e) {
            log.error("", e);
        }

        instrumentation.removeTransformer(transformer);

    }
}

class MethodParameterLogTransformer implements ClassFileTransformer {

    private static final Logger log = LoggerFactory.getLogger(MethodParameterLogTransformer.class);

    private static final List<String> PACKAGE_NAME_LIST = Lists.newArrayList("java.util",
            "com.alibaba.fastjson2");

    private final String targetClassName;

    private final String methodName;

    private final ClassPool classPool;

    public MethodParameterLogTransformer(String targetClassName, String methodName) {
        this.targetClassName = targetClassName;
        this.methodName = methodName;
        this.classPool = ClassPool.getDefault();

        importPackages(classPool);
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) {

        // class not match
        String classNameDot = className.replace("/", ".");
        if (!targetClassName.equals(classNameDot)) {
            return classfileBuffer;
        }

        try {
            CtClass ctClass = classPool.get(classBeingRedefined.getName());
            CtMethod ctMethod = ctClass.getDeclaredMethod(methodName);

            List<String> paramNameList = getParamNameList(ctMethod);
            try {
                ctMethod.insertBefore(generateLogLineCode(methodName, paramNameList));
            } catch (CannotCompileException e) {
                log.error("", e);
                return classfileBuffer;
            }
            ctClass.detach();

            return ctClass.toBytecode();
        } catch (NotFoundException e) {
            log.error("can not find the class[{}] or method[{}]", classNameDot, methodName);
        } catch (IOException | CannotCompileException e) {
            log.error("", e);
        }

        return classfileBuffer;
    }

    private String generateLogLineCode(String methodName, List<String> paramNameList) {
        String format = String.format("new Date() + \" [enhance] INFO \" + this.getClass()" +
                ".getName() + \".%s | \" + %s", methodName, generateParamValue(paramNameList));

        return String.format("System.out.println(%s);", format);
    }

    private String generateParamValue(List<String> paramNameList) {
        return paramNameList.stream()
                .map(name -> String.format(" \" %s:\" + JSON.toJSONString" + "(%s)", name, name))
                .collect(Collectors.joining(" + "));
    }

    private List<String> getParamNameList(CtMethod ctMethod) {
        MethodInfo methodInfo = ctMethod.getMethodInfo();
        LocalVariableAttribute codeAttribute =
                (LocalVariableAttribute) methodInfo.getCodeAttribute().getAttribute(LocalVariableAttribute.tag);

        List<String> paramNameList = new ArrayList<>();

        if (codeAttribute != null) {
            int len;
            try {
                len = ctMethod.getParameterTypes().length;
            } catch (NotFoundException e) {
                return paramNameList;
            }

            // 跳过this
            int posOffset = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
            for (int i = 0; i < len; i++) {
                paramNameList.add(codeAttribute.variableName(i + posOffset));
            }
        }

        return paramNameList;
    }

    private void importPackages(ClassPool classPool) {
        for (String packageName : PACKAGE_NAME_LIST) {
            classPool.importPackage(packageName);
        }
    }
}
