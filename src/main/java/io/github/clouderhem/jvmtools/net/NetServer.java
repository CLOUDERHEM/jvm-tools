package io.github.clouderhem.jvmtools.net;

import io.netty.channel.EventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Aaron Yeung
 * @date 8/18/2023 5:10 PM
 */
public class NetServer {

    private static final Logger log = LoggerFactory.getLogger(NetServer.class);

    private static EventLoopGroup bossGroup;
    private static EventLoopGroup workerGroup;

    public static void setServerGroup(EventLoopGroup bossGroupParam,
                                      EventLoopGroup workerGroupParam) {
        bossGroup = bossGroupParam;
        workerGroup = workerGroupParam;
    }

    public static void shutdownServer() {
        log.info("Tools server shutdown now");

        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

}
