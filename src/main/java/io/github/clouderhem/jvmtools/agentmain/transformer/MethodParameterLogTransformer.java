package io.github.clouderhem.jvmtools.agentmain.transformer;

import com.google.common.collect.Lists;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Aaron Yeung
 * @date 8/18/2023 4:36 PM
 */
public class MethodParameterLogTransformer implements ClassFileTransformer {

    private static final Logger log = LoggerFactory.getLogger(MethodParameterLogTransformer.class);

    private static final List<String> PACKAGE_NAME_LIST = Lists.newArrayList("java.util", "com.alibaba.fastjson2",
            "io.github.clouderhem.jvmtools.common.log");

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

        preProcessClassPool(loader);

        // class not match
        String classNameDot = className.replace("/", ".");
        if (!targetClassName.equals(classNameDot)) {
            return classfileBuffer;
        }

        try {
            CtClass ctClass = classPool.get(classBeingRedefined.getName());
            CtMethod ctMethod = ctClass.getDeclaredMethod(methodName);

            List<String> paramNameList = getParamNameList(ctMethod);
            log.info("Transforming classFile, className:{}", classNameDot);
            ctMethod.insertAfter(generateParametersLogLineCode(methodName, paramNameList));

            ctClass.detach();

            return ctClass.toBytecode();
        } catch (Exception e) {
            log.error("", e);
        }

        return classfileBuffer;
    }

    private String generateParametersLogLineCode(String methodName, List<String> paramNameList) {
        return generateLogLineCode("Parameters Log", methodName, paramNameList);
    }

    private String generateLogLineCode(String logDesc, String methodName, List<String> valueNameList) {

        String value = valueNameList.stream().map(name -> String.format(" \" %s:\" + JSON" + ".toJSONString" + "(%s)"
                , name, name)).collect(Collectors.joining(" + "));

        String logLineCode =
                String.format("new Date() + \" [%s] INFO \" + this.getClass()" + ".getName() + \".%s - " + "\" + %s",
                        logDesc, methodName, value);

        return String.format("LogEntry.info(%s);", logLineCode);
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

    private void preProcessClassPool(ClassLoader classLoader) {
        ClassPool.getDefault().appendClassPath(new LoaderClassPath(classLoader));
    }

}
