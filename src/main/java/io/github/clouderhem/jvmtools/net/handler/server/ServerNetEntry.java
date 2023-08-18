package io.github.clouderhem.jvmtools.net.handler.server;

import io.github.clouderhem.jvmtools.agentmain.strategy.CmdProcessStrategy;
import io.github.clouderhem.jvmtools.agentmain.strategy.CmdProcessStrategyFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;
import java.util.Optional;

/**
 * @author Aaron Yeung
 * @date 8/18/2023 2:53 PM
 */
public class ServerNetEntry extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(ServerNetEntry.class);

    private final Instrumentation instrumentation;

    public ServerNetEntry(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msgParam) {
        String msg = ((ByteBuf) msgParam).toString(CharsetUtil.UTF_8);
        log.info("Server received | remoteAddress:{}, message:{}", ctx.channel().remoteAddress(),
                msg);

        Optional<CmdProcessStrategy> strategy = CmdProcessStrategyFactory.findStrategy(msg);

        if (!strategy.isPresent()) {
            log.error("Command [{}] is not support", msg);
            return;
        }

        strategy.get().process(msg, this.instrumentation);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("", cause);
        ctx.close();
    }
}
