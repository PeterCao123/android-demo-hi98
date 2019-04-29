package com.shiji.png.netty.server.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

/**
 * @author bruce.wu
 * @since 2018/12/4 11:10
 */
@ChannelHandler.Sharable
public class HttpHandler extends SimpleChannelInboundHandler<HttpRequest> {

    private static final byte[] CONTENT = { 'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd' };

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpRequest request) throws Exception {
        boolean keepAlive = HttpUtil.isKeepAlive(request);

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.wrappedBuffer(CONTENT));
        response.headers().set("Content-Type", "text/plain");
        response.headers().setInt("Content-Length", response.content().readableBytes());

        if (!keepAlive) {
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        } else {
            response.headers().set("Connection", "keep-alive");
            ctx.writeAndFlush(response);
        }
    }

}
