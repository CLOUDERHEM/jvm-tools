package io.github.clouderhem.jvmtools;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Aaron Yeung
 * @date 8/16/2023 8:56 PM
 */
public class Startup {

    private static final Logger log = LoggerFactory.getLogger(Startup.class);

//    public static void main(String[] args) throws IOException, AttachNotSupportedException,
    //            AgentLoadException, AgentInitializationException {
    //
    //        if (args.length == 0) {
    //            log.error("Pid of java app can not be null/empty");
    //        }
    //
    //        VirtualMachine vm = VirtualMachine.attach(args[0]);
    //
    //        vm.loadAgent(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath
    //                ().substring(1), String.join(" ", args));
    //    }
        public static void main(String[] args) throws IOException, AttachNotSupportedException,
                AgentLoadException, AgentInitializationException {


            VirtualMachine vm = VirtualMachine.attach("10668");

            vm.loadAgent("H:\\all-projects\\IdeaProjects\\javaagnet\\build\\libs\\jvm-tools-1" +
                    ".0-SNAPSHOT.jar", "method com.lc.OneController sayHello 10");
        }
}