package com.shiji.png.netty.server.handler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpUtil;

/**
 * @author bruce.wu
 * @date 2018/10/22
 */
@Deprecated
public abstract class AbstractHttpHandler extends AbstractHandler<HttpRequest> {

    @Override
    protected void handleMessage(ChannelHandlerContext ctx, HttpRequest request) throws Exception {
        boolean keepAlive = HttpUtil.isKeepAlive(request);

        HttpResponse response = getResponse(request);

        if (!keepAlive) {
            ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        } else {
            response.headers().set("Connection", "keep-alive");
            ctx.write(response);
        }
    }

    protected abstract HttpResponse getResponse(HttpRequest request);

}
