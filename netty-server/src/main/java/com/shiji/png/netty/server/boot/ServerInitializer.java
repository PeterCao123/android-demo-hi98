package com.shiji.png.netty.server.boot;

import com.shiji.png.netty.server.config.ServerConfig;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author bruce.wu
 * @date 2018/9/21
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    private final ServerConfig config;

    ServerInitializer(ServerConfig config) {
        this.config = config;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        config.getSecureStrategy().apply(ch);
        config.getCodecStrategy().apply(ch);
        pipeline.addLast(config.getHandler());
    }

}
