package com.shiji.png.pinpad.agent.codec;

import com.shiji.png.netty.server.codec.CodecStrategy;
import com.shiji.png.netty.server.codec.TwoBytesLengthTextCodecStrategy;
import com.shiji.png.pinpad.agent.model.TransactionDto;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author bruce.wu
 * @date 2018/10/19
 */
public class TwoBytesLengthJsonCodecStrategy
        extends TwoBytesLengthTextCodecStrategy implements CodecStrategy {

    private final Class<?> type;

    public TwoBytesLengthJsonCodecStrategy() {
        this(TransactionDto.class);
    }

    public TwoBytesLengthJsonCodecStrategy(Class<?> type) {
        this.type = type;
    }

    @Override
    public void apply(SocketChannel ch) throws Exception {
        super.apply(ch);

        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new JsonDecoder(type));
        pipeline.addLast(new JsonEncoder());
    }

    @Override
    public String toString() {
        return "TwoBytesLengthJsonCodecStrategy: type=" + type.getSimpleName();
    }

}
