package io.github.clouderhem.jvmtools.net;

import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * @author Aaron Yeung
 * @date 8/18/2023 5:40 PM
 */
public class NetClient {

    private static NioEventLoopGroup nioEventLoopGroup;

    private static Channel channel;

    public static void setClientGroupAndChannel(NioEventLoopGroup nioEventLoopGroupParam,
                                                Channel channelParam) {
        nioEventLoopGroup = nioEventLoopGroupParam;
        channel = channelParam;
    }

    public static void shutdownClient() {
        channel.close();
        nioEventLoopGroup.shutdownGracefully();
        System.exit(0);
    }
}
