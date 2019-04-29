package com.shiji.png.netty.server.codec;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;


/**
 * @author bruce.wu
 * @date 2018/9/21
 */
public class HttpCodecStrategy implements CodecStrategy {
    @Override
    public void apply(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpServerExpectContinueHandler());
    }

    @Override
    public String toString() {
        return "HttpCodecStrategy";
    }
}
