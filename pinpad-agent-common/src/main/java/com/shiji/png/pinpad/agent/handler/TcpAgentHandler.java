package com.shiji.png.pinpad.agent.handler;

import com.shiji.png.pinpad.agent.model.TransactionDto;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author bruce.wu
 * @since 2018/12/6 9:34
 */
@ChannelHandler.Sharable
public class TcpAgentHandler extends SimpleChannelInboundHandler<TransactionDto> {

    private final CommandProcessor commandProcessor;

    public TcpAgentHandler(CommandProcessor commandProcessor) {
        super();
        this.commandProcessor = commandProcessor;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TransactionDto msg) throws Exception {
        TransactionDto dto = commandProcessor.process(msg);
        ctx.writeAndFlush(dto);
        ctx.close();
    }

}
