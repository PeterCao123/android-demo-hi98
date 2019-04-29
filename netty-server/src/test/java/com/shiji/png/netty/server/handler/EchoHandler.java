package com.shiji.png.netty.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bruce.wu
 * @since 2018/12/4 11:08
 */
@ChannelHandler.Sharable
public class EchoHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger logger = LoggerFactory.getLogger(EchoHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        logger.info("{}", s);
        ctx.writeAndFlush(s);
    }

}
