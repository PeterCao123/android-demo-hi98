package com.shiji.png.netty.server.secure;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslHandler;

/**
 * @author bruce.wu
 * @date 2018/9/25
 */
public abstract class ClientSecureStrategy implements SecureStrategy {

    final String host;
    final int port;

    public ClientSecureStrategy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void apply(SocketChannel ch) {
        ch.pipeline().addLast(newSslHandler(ch.alloc()));
    }

    protected abstract SslHandler newSslHandler(ByteBufAllocator allocator);

    @Override
    public String toString() {
        return "ClientSecureStrategy";
    }

}
