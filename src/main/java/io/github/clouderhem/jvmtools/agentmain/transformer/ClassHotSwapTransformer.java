package io.github.clouderhem.jvmtools.agentmain.transformer;

import io.github.clouderhem.jvmtools.common.util.ClassUtils;
import io.github.clouderhem.jvmtools.common.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

/**
 * @author Aaron Yeung
 * @date 8/18/2023 9:17 PM
 */
public class ClassHotSwapTransformer implements ClassFileTransformer {

    private static final Logger log = LoggerFactory.getLogger(ClassHotSwapTransformer.class);

    private final String targetClassName;

    private final String classFileUrl;

    public ClassHotSwapTransformer(String targetClassName, String classFileUrl) {
        this.targetClassName = targetClassName;
        this.classFileUrl = classFileUrl;
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
            byte[] classBytecode = FileUtils.downloadFileFromUrl(classFileUrl);
            postProcessClassFile(classBytecode);

            log.info("HotSwap classFile, className:{}, classFileUrl:{}", classNameDot,
                    classFileUrl);

            return classBytecode;
        } catch (Exception e) {
            log.error("", e);
            return classfileBuffer;
        }

    }

    private void postProcessClassFile(byte[] classBytecode) {
        ClassUtils.checkClassBytecodeValid(classBytecode);
    }
}
