package com.shiji.png.pinpad.agent.codec;

import com.shiji.png.payment.util.Json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

/**
 * @author bruce.wu
 * @since 2018/10/22
 */
public class JsonDecoder extends MessageToMessageDecoder<CharSequence> {

    private static final Logger logger = LoggerFactory.getLogger(JsonDecoder.class);

    private final Class<?> type;

    public JsonDecoder(Class<?> type) {
        this.type = type;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, CharSequence msg, List<Object> out) throws Exception {
        logger.trace("recv: {}", msg);
        out.add(Json.getDecoder().decode(msg.toString(), type));
    }

}
