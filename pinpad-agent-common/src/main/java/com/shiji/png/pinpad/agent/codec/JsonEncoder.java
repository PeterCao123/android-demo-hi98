package com.shiji.png.pinpad.agent.codec;

import com.shiji.png.payment.util.Json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

/**
 * @author bruce.wu
 * @date 2018/10/22
 */
public class JsonEncoder extends MessageToMessageEncoder<Object> {

    private static final Logger logger = LoggerFactory.getLogger(JsonDecoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        if (msg == null) {
            return;
        }
        String json = Json.getEncoder().encode(msg);
        logger.trace("send: {}", json);
        out.add(json);
    }

}
