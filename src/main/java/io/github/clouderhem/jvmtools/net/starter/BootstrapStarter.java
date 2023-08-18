package io.github.clouderhem.jvmtools.net.starter;

import io.github.clouderhem.jvmtools.agentmain.common.Constants;
import io.github.clouderhem.jvmtools.net.NetClient;
import io.github.clouderhem.jvmtools.net.handler.client.ClientNetEntry;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author Aaron Yeung
 * @date 8/18/2023 4:46 PM
 */
public class BootstrapStarter extends Thread {
    @Override
    public void run() {

        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();


            bootstrap.group(eventExecutors)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new ClientNetEntry());
                        }
                    });


            ChannelFuture connect = bootstrap.connect(Constants.SERVER_IP,
                    Constants.SERVER_PORT);
            NetClient.setClientGroupAndChannel(eventExecutors, connect.channel());
            ChannelFuture channelFuture = connect.sync();
            channelFuture.channel().closeFuture().sync();


        } catch (Exception e) {
            eventExecutors.shutdownGracefully();
        }
    }
}
