package io.github.clouderhem.jvmtools;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import io.github.clouderhem.jvmtools.net.starter.BootstrapStarter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Aaron Yeung
 * @date 8/16/2023 8:56 PM
 */
public class Startup {

    private static final Logger log = LoggerFactory.getLogger(Startup.class);

    public static void main(String[] args) throws IOException, AttachNotSupportedException,
            AgentLoadException, AgentInitializationException {

        if (args.length == 0 || StringUtils.isEmpty(args[0])) {
            log.error("Pid cannot be null/empty");
        }

        VirtualMachine vm = VirtualMachine.attach(args[0]);
        vm.loadAgent(Startup.class.getProtectionDomain().
                getCodeSource().getLocation().getPath().substring(1));

        new BootstrapStarter().start();
    }

}
