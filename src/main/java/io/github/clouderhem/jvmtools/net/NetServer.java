package io.github.clouderhem.jvmtools.net;

import io.netty.channel.EventLoopGroup;

/**
 * @author Aaron Yeung
 * @date 8/18/2023 5:10 PM
 */
public class NetServer {

    private static EventLoopGroup bossGroup;
    private static EventLoopGroup workerGroup;

    public static void setServerGroup(EventLoopGroup bossGroupParam,
                                      EventLoopGroup workerGroupParam) {
        bossGroup = bossGroupParam;
        workerGroup = workerGroupParam;
    }

    public static void shutdownServer() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

}
