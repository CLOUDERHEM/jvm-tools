package io.github.clouderhem.jvmtools.agentmain.common;

/**
 * @author Aaron Yeung
 * @date 8/18/2023 2:03 PM
 */
public interface ClassStore {
    void storeClassFile(String className, byte[] classfileBuffer);
}
