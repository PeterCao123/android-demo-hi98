package com.shiji.png.netty.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @see io.netty.channel.SimpleChannelInboundHandler
 *
 * @author bruce.wu
 * @date 2018/9/21
 */
@Deprecated
@ChannelHandler.Sharable
public abstract class AbstractHandler<T> extends ChannelInboundHandlerAdapter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Class<T> msgType = getActualType();

    protected abstract void handleMessage(ChannelHandlerContext ctx, T msg) throws Exception;

    protected void handleUnexpected(ChannelHandlerContext ctx, Object msg) throws Exception {

    }

    @SuppressWarnings("unchecked")
    private Class<T> getActualType() {
        return (Class<T>)getParameterizedType().getActualTypeArguments()[0];
    }

    private ParameterizedType getParameterizedType() {
        Class<?> type = getClass();
        while (!type.getSuperclass().equals(AbstractHandler.class)) {
            type = type.getSuperclass();
        }
        return (ParameterizedType)type.getGenericSuperclass();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        logger.info("accept connection: {}", ctx.channel().remoteAddress());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msgType.isAssignableFrom(msg.getClass())) {
            handleMessage(ctx, (T)msg);
            return;
        }
        handleUnexpected(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("{}", cause);
        ctx.close();
    }

}
