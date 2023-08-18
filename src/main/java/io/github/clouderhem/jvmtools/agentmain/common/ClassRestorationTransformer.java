package io.github.clouderhem.jvmtools.agentmain.common;

import com.google.common.collect.Maps;
import io.github.clouderhem.jvmtools.agentmain.strategy.impl.MethodParameterLogStrategyImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.Map;

/**
 * @author Aaron Yeung
 * @date 8/18/2023 1:55 PM
 */
public class ClassRestorationTransformer implements ClassFileTransformer {

    public static final Map<String, byte[]> CLASS_BYTES_MAP = Maps.newHashMap();

    private static final Logger log = LoggerFactory.getLogger(MethodParameterLogStrategyImpl.class);

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) {

        // class not match
        String classNameDot = className.replace("/", ".");
        if (!CLASS_BYTES_MAP.containsKey(classNameDot)) {
            return classfileBuffer;
        }

        log.info("restore class file, className={}", classNameDot);

        byte[] bytes = CLASS_BYTES_MAP.get(classNameDot);
        CLASS_BYTES_MAP.remove(classNameDot);

        return bytes;
    }
}
