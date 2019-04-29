package com.shiji.png.netty.server.secure;

import io.netty.channel.socket.SocketChannel;

/**
 * @author bruce.wu
 * @since 2018/10/19
 */
public interface SecureStrategy {

    void apply(SocketChannel ch);

}
