package io.github.clouderhem.jvmtools.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.Set;

/**
 * @author Aaron Yeung
 * @date 8/23/2023 12:08 PM
 */
public class InstrumentationUtils {

    private static final Logger log = LoggerFactory.getLogger(InstrumentationUtils.class);

    public static void retransformClasses(Instrumentation instrumentation,
                                          Set<String> classNameSet) {
        for (Class clazz : instrumentation.getAllLoadedClasses()) {
            if (classNameSet.contains(clazz.getName())) {
                try {
                    instrumentation.retransformClasses(clazz);
                } catch (UnmodifiableClassException e) {
                    log.error("Can not modify class:{}", clazz, e);
                }
            }
        }
    }
}
