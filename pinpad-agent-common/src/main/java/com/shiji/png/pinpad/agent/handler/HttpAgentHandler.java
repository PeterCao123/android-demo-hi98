package com.shiji.png.pinpad.agent.handler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpUtil;

/**
 * @author bruce.wu
 * @since 2018/12/6 9:35
 */
@ChannelHandler.Sharable
public abstract class HttpAgentHandler extends SimpleChannelInboundHandler<HttpRequest> {

    @Override
    protected final void channelRead0(ChannelHandlerContext ctx, HttpRequest request) throws Exception {
        boolean keepAlive = HttpUtil.isKeepAlive(request);
        FullHttpResponse response = getResponse(request);
        response.headers().setInt("Content-Length", response.content().readableBytes());
        if (!keepAlive) {
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        } else {
            response.headers().set("Connection", "keep-alive");
            ctx.writeAndFlush(response);
        }
    }

    abstract FullHttpResponse getResponse(HttpRequest request);

}
