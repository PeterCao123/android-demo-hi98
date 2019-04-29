package com.shiji.png.netty.server.codec;

import io.netty.channel.socket.SocketChannel;

/**
 * @author bruce.wu
 * @date 2018/10/19
 */
public interface CodecStrategy {

    void apply(SocketChannel ch) throws Exception;

}
