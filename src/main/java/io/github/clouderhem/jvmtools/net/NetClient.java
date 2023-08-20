package io.github.clouderhem.jvmtools.net;

import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Aaron Yeung
 * @date 8/18/2023 5:40 PM
 */
public class NetClient {

    private static final Logger log = LoggerFactory.getLogger(NetClient.class);

    private static NioEventLoopGroup nioEventLoopGroup;

    private static Channel channel;

    public static void setClientGroupAndChannel(NioEventLoopGroup nioEventLoopGroupParam,
                                                Channel channelParam) {
        nioEventLoopGroup = nioEventLoopGroupParam;
        channel = channelParam;
    }

    public static void shutdownClient() {
        log.info("Tools client close now");

        channel.close();
        nioEventLoopGroup.shutdownGracefully();
        System.exit(0);
    }
}
