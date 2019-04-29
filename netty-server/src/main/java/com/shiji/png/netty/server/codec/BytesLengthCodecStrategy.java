package com.shiji.png.netty.server.codec;

import java.nio.ByteOrder;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * @author bruce.wu
 * @date 2018/10/19
 */
public class BytesLengthCodecStrategy implements CodecStrategy {

    private final ByteOrder byteOrder;
    private final int maxFrameLength;
    private final int lengthFieldOffset;
    private final int lengthFieldLength;
    private final int lengthAdjustment;
    private final int initialBytesToStrip;
    private final boolean failFast;

    public BytesLengthCodecStrategy(ByteOrder byteOrder,
                                    int maxFrameLength,
                                    int lengthFieldOffset,
                                    int lengthFieldLength,
                                    int lengthAdjustment,
                                    int initialBytesToStrip,
                                    boolean failFast) {
        this.byteOrder = byteOrder;
        this.maxFrameLength = maxFrameLength;
        this.lengthFieldOffset = lengthFieldOffset;
        this.lengthFieldLength = lengthFieldLength;
        this.lengthAdjustment = lengthAdjustment;
        this.initialBytesToStrip = initialBytesToStrip;
        this.failFast = failFast;
    }

    @Override
    public void apply(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(this.byteOrder,
                this.maxFrameLength, this.lengthFieldOffset, this.lengthFieldLength,
                this.lengthAdjustment, this.initialBytesToStrip, this.failFast));
        pipeline.addLast(new LengthFieldPrepender(ByteOrder.BIG_ENDIAN,
                this.lengthFieldLength, this.lengthAdjustment, false));
    }

    @Override
    public String toString() {
        return "BytesLengthCodecStrategy";
    }
}
