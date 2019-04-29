package com.shiji.png.netty.server.secure;

import io.netty.channel.socket.SocketChannel;

/**
 * @author bruce.wu
 * @since 2018/9/25
 */
public class NonSecureStrategy implements SecureStrategy {
    @Override
    public void apply(SocketChannel ch) {

    }

    @Override
    public String toString() {
        return "NonSecureStrategy";
    }
}
