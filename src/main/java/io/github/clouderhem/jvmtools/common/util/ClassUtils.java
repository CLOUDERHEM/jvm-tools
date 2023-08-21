package io.github.clouderhem.jvmtools.common.util;

import io.github.clouderhem.jvmtools.common.data.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Aaron Yeung
 * @date 8/18/2023 9:56 PM
 */
public class ClassUtils {

    private static final Logger log = LoggerFactory.getLogger(ClassUtils.class);

    public static void checkClassBytecodeValid(byte[] classBytecode) {
        // todo
    }

    public static Class<?> findClass(String className) throws ClassNotFoundException {

        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.info("Class.forName({}) can not find the Class", className);
        }

        List<Pair<Thread, ClassLoader>> classLoaderList = findContextClassLoaderListFromThreads();

        for (Pair<Thread, ClassLoader> threadClassLoaderPair : classLoaderList) {
            try {
                Class<?> clazz = Class.forName(className, false, threadClassLoaderPair.getSecond());
                log.info("Loaded class:{} by ClassLoader:{}, threadName:{}", className,
                        threadClassLoaderPair.getSecond(), threadClassLoaderPair.getFirst());
                return clazz;
            } catch (ClassNotFoundException e) {
                log.info("Can not find the Class:{} with ClassLoader:{}", className,
                        threadClassLoaderPair.getSecond());
            }
        }

        throw new ClassNotFoundException(className);
    }

    public static List<Pair<Thread, ClassLoader>> findContextClassLoaderListFromThreads() {

        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        Thread[] threadList = new Thread[threadGroup.activeCount()];
        threadGroup.enumerate(threadList);

        return Arrays.stream(threadList).map(thread -> {
            if (thread.getContextClassLoader() == null) {
                return null;
            }
            return new Pair<>(thread, thread.getContextClassLoader());
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
