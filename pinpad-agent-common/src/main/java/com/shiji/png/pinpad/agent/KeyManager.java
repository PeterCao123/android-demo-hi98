package com.shiji.png.pinpad.agent;

import com.shiji.png.common.enums.TransactionType;
import com.shiji.png.pinpad.agent.crypto.Key;
import com.shiji.png.pinpad.agent.model.SpaConfig;
import com.shiji.png.pinpad.agent.model.SpaMsg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author bruce.wu
 * @since 2018/12/6 11:08
 */
public class KeyManager {

    private static final Logger logger = LoggerFactory.getLogger("KeyManager");

    public static Key download(SpaConfig config) throws Exception {
        Key key = new Key();
        SpaMsg msg;

        msg = downloadKEK(config);
        key.setKEK(msg.getEncryptedKek(), msg.getCheckValueKek());

        msg = downloadDEK(config);
        key.setDTK(msg.getEncryptedDek(), msg.getCheckValueDek());

        logger.info("KEK and DEK is downloaded");

        return key;
    }

    private static SpaMsg downloadKEK(SpaConfig config) throws Exception {
        logger.info("start download KEK...");
        SpaMsg dto = SpaMsg.builder()
                .txnType(TransactionType.DOWNLOAD_KEK.getType())
                .type("0")
                .merId(config.getMerId())
                .terId(config.getTerId())
                .mName(config.getModuleName())
                .mVersion(config.getModuleVersion())
                .build();
        return download(config, dto);
    }

    private static SpaMsg downloadDEK(SpaConfig config) throws Exception {
        logger.info("start download DEK...");
        SpaMsg dto = SpaMsg.builder()
                .txnType(TransactionType.DOWNLOAD_DEK.getType())
                .type("0")
                .merId(config.getMerId())
                .terId(config.getTerId())
                .mName(config.getModuleName())
                .mVersion(config.getModuleVersion())
                .build();
        return download(config, dto);
    }

    private static SpaMsg download(SpaConfig config, SpaMsg request) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            RpcClientHandler handler = new RpcClientHandler(request);
            ChannelFuture f = new io.netty.bootstrap.Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, config.getConnectTimeout() * 1000)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            config.getSecureStrategy().apply(ch);
                            config.getCodecStrategy().apply(ch);
                            ch.pipeline().addLast(handler);
                        }
                    })
                    .connect(config.getHost(), config.getPort())
                    .sync();
            SpaMsg result = handler.getAnswer(config.getReadTimeout());
            f.channel().close();

            if (result == null) {
                throw new RuntimeException("read timeout");
            }
            return result;
        } finally {
            group.shutdownGracefully();
        }
    }

    @ChannelHandler.Sharable
    private static class RpcClientHandler extends SimpleChannelInboundHandler<SpaMsg> {

        private final BlockingQueue<SpaMsg> answer = new LinkedBlockingDeque<>(1);

        private final SpaMsg request;

        RpcClientHandler(SpaMsg request) {
            this.request = request;
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, SpaMsg msg) throws Exception {
            answer.offer(msg);
        }

        SpaMsg getAnswer(int timeout) throws Exception {
            return answer.poll(timeout, TimeUnit.SECONDS);
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ctx.writeAndFlush(request);
        }

    }

}
