package io.github.clouderhem.jvmtools.net.starter;

import io.github.clouderhem.jvmtools.agentmain.common.Constants;
import io.github.clouderhem.jvmtools.net.NetServer;
import io.github.clouderhem.jvmtools.net.handler.server.ServerAutoShutdownHandler;
import io.github.clouderhem.jvmtools.net.handler.server.ServerNetEntry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;

/**
 * @author Aaron Yeung
 * @date 8/18/2023 4:34 PM
 */
public class ServerBootstrapStarter extends Thread {

    private static final Logger log = LoggerFactory.getLogger(ServerBootstrapStarter.class);

    private final Instrumentation instrumentation;

    public ServerBootstrapStarter(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    @Override
    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        NetServer.setServerGroup(bossGroup, workerGroup);

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline()
                                    .addLast(new ServerAutoShutdownHandler())
                                    .addLast(new ServerNetEntry(instrumentation));
                        }
                    });

            log.info("Tools server is listening on [{}]", Constants.SERVER_PORT);

            ChannelFuture channelFuture = bootstrap.bind(Constants.SERVER_PORT).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("", e);
        }
    }
}
