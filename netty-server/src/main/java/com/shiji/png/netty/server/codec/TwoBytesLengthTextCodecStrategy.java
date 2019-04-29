package com.shiji.png.netty.server.codec;

import java.nio.ByteOrder;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author bruce.wu
 * @date 2018/10/19
 */
public class TwoBytesLengthTextCodecStrategy
        extends BytesLengthCodecStrategy implements CodecStrategy {

    public TwoBytesLengthTextCodecStrategy() {
        super(ByteOrder.BIG_ENDIAN, Short.MAX_VALUE,
                0, 2,
                0, 2,
                true);
    }

    @Override
    public void apply(SocketChannel ch) throws Exception {
        super.apply(ch);

        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new StringEncoder());
    }

    @Override
    public String toString() {
        return "TwoBytesLengthTextCodecStrategy";
    }
}
