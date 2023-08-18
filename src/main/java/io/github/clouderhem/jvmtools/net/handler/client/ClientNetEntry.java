package io.github.clouderhem.jvmtools.net.handler.client;

import io.github.clouderhem.jvmtools.agentmain.strategy.impl.ShutdownStrategyImpl;
import io.github.clouderhem.jvmtools.net.NetClient;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * @author Aaron Yeung
 * @date 8/18/2023 3:16 PM
 */
public class ClientNetEntry extends ChannelInboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(ClientNetEntry.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please input cmd = ");
        while (scanner.hasNext()) {
            String cmd = scanner.nextLine();
            ctx.writeAndFlush(Unpooled.copiedBuffer(cmd, CharsetUtil.UTF_8));
            postProcess(ctx, cmd);
            System.out.print("\nPlease input cmd = ");
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String msgStr = ((ByteBuf) msg).toString(CharsetUtil.UTF_8);
        log.info("Receive, remoteAddress={}. msg={},", ctx.channel().remoteAddress(), msgStr);
    }

    private void postProcess(ChannelHandlerContext ctx, String cmd) {
        if (ShutdownStrategyImpl.APPLY_OP.equals(cmd)) {
            NetClient.shutdownClient();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("", cause);
    }
}
