package io.github.clouderhem.jvmtools.net.handler.server;

import io.github.clouderhem.jvmtools.net.NetServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.ReadTimeoutHandler;

import static io.github.clouderhem.jvmtools.agentmain.common.Constants.MAX_SHUTDOWN_TIMEOUT;

/**
 * @author Aaron Yeung
 * @date 8/20/2023 4:50 PM
 */
public class ServerAutoShutdownHandler extends ReadTimeoutHandler {

    public ServerAutoShutdownHandler() {
        super(MAX_SHUTDOWN_TIMEOUT);
    }

    @Override
    protected void readTimedOut(ChannelHandlerContext ctx) {
        NetServer.shutdownServer();
    }

}
