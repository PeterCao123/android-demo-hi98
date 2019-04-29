package com.shiji.png.netty.server;

import com.shiji.png.netty.server.codec.TwoBytesLengthTextCodecStrategy;
import com.shiji.png.netty.server.config.ServerConfig;
import com.shiji.png.netty.server.handler.EchoHandler;
import com.shiji.png.netty.server.secure.JKSClientMutualStrategy;
import com.shiji.png.netty.server.secure.JKSClientSecureStrategy;
import com.shiji.png.netty.server.secure.JKSMutualSecureStrategy;
import com.shiji.png.netty.server.secure.JKSServerSecureStrategy;
import com.shiji.png.netty.server.secure.NonSecureStrategy;
import com.shiji.png.netty.server.secure.PEMMutualSecureStrategy;
import com.shiji.png.netty.server.secure.PEMServerSecureStrategy;
import com.shiji.png.netty.server.secure.SecureStrategy;

import org.junit.Test;

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

import static org.junit.Assert.assertEquals;

/**
 * @author bruce.wu
 * @since 2018/12/4 14:43
 */
public class EchoClientTest {

    private static final EchoHandler echoHandler = new EchoHandler();

    @Test
    public void non_secure() throws Exception {
        String host = "localhost";
        int port = 8101;
        SecureStrategy strategy = new NonSecureStrategy();
        String message = "hello";

        ServerConfig config = ServerConfig.builder()
                .port(port)
                .codecStrategy(new TwoBytesLengthTextCodecStrategy())
                .handler(echoHandler)
                .build();

        ServerRunner runner = new ServerRunner(config);
        runner.runInBackground();
        try {
            String result = call(host, port, strategy, message);
            assertEquals(message, result);
        } finally {
            runner.stop();
        }
    }

    @Test
    public void server_jks() throws Exception {
        String host = "localhost";
        int port = 8102;
        SecureStrategy strategy = new JKSClientSecureStrategy(host, port,
                Certificate.CLIENT_JKS_STORE, Certificate.CLIENT_JKS_PASSWORD);
        String message = "world";

        ServerConfig config = ServerConfig.builder()
                .port(port)
                .secureStrategy(new JKSServerSecureStrategy(Certificate.SERVER_JKS_STORE, Certificate.SERVER_JKS_PASSWORD, Certificate.SERVER_KEY_PASSWORD))
                .codecStrategy(new TwoBytesLengthTextCodecStrategy())
                .handler(echoHandler)
                .build();

        ServerRunner runner = new ServerRunner(config);
        runner.runInBackground();
        try {
            String result = call(host, port, strategy, message);
            assertEquals(message, result);
        } finally {
            runner.stop();
        }
    }

    @Test
    public void mutual_jks() throws Exception {
        String host = "localhost";
        int port = 8103;
        SecureStrategy strategy = new JKSClientMutualStrategy(host, port,
                Certificate.CLIENT_JKS_STORE, Certificate.CLIENT_JKS_PASSWORD,
                Certificate.CLIENT_KEY_PASSWORD);
        String message = "hello world";

        ServerConfig config = ServerConfig.builder()
                .port(port)
                .secureStrategy(new JKSMutualSecureStrategy(Certificate.SERVER_JKS_STORE, Certificate.SERVER_JKS_PASSWORD, Certificate.SERVER_KEY_PASSWORD))
                .codecStrategy(new TwoBytesLengthTextCodecStrategy())
                .handler(echoHandler)
                .build();

        ServerRunner runner = new ServerRunner(config);
        runner.runInBackground();
        try {
            String result = call(host, port, strategy, message);
            assertEquals(message, result);
        } finally {
            runner.stop();
        }
    }

    @Test
    public void server_pem() throws Exception {
        String host = "localhost";
        int port = 8104;
        SecureStrategy strategy = new JKSClientSecureStrategy(host, port,
                Certificate.CLIENT_JKS_STORE, Certificate.CLIENT_JKS_PASSWORD);
        String message = "server_pem";

        ServerConfig config = ServerConfig.builder()
                .port(port)
                .secureStrategy(new PEMServerSecureStrategy(Certificate.SERVER_PEM_CERT_FILE, Certificate.SERVER_PEM_KEY_FILE, Certificate.SERVER_PEM_KEY_PASSWORD))
                .codecStrategy(new TwoBytesLengthTextCodecStrategy())
                .handler(echoHandler)
                .build();

        ServerRunner runner = new ServerRunner(config);
        runner.runInBackground();
        try {
            String result = call(host, port, strategy, message);
            assertEquals(message, result);
        } finally {
            runner.stop();
        }
    }

    @Test
    public void mutual_pem() throws Exception {
        String host = "localhost";
        int port = 8105;
        SecureStrategy strategy = new JKSClientMutualStrategy(host, port,
                Certificate.CLIENT_JKS_STORE, Certificate.CLIENT_JKS_PASSWORD,
                Certificate.CLIENT_KEY_PASSWORD);
        String message = "mutual_pem";

        ServerConfig config = ServerConfig.builder()
                .port(port)
                .secureStrategy(new PEMMutualSecureStrategy(Certificate.SERVER_PEM_CERT_FILE, Certificate.SERVER_PEM_KEY_FILE, Certificate.SERVER_PEM_KEY_PASSWORD, Certificate.SERVER_PEM_TRUST_FILE))
                .codecStrategy(new TwoBytesLengthTextCodecStrategy())
                .handler(echoHandler)
                .build();

        ServerRunner runner = new ServerRunner(config);
        runner.runInBackground();
        try {
            String result = call(host, port, strategy, message);
            assertEquals(message, result);
        } finally {
            runner.stop();
        }
    }


    private String call(String host, int port, SecureStrategy secureStrategy, String request) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ClientHandler handler = new ClientHandler(request);
            ChannelFuture f = new io.netty.bootstrap.Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    //.option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            secureStrategy.apply(ch);
                            new TwoBytesLengthTextCodecStrategy().apply(ch);
                            ch.pipeline().addLast(handler);
                        }
                    })
                    .connect(host, port)
                    .sync();
            String result = handler.getAnswer(1);
            f.channel().close();
            return result;
        } finally {
            group.shutdownGracefully();
        }
    }


    @ChannelHandler.Sharable
    static class ClientHandler extends SimpleChannelInboundHandler<String> {

        private final String request;

        private final BlockingQueue<String> answer = new LinkedBlockingDeque<>(1);

        ClientHandler(String request) {
            this.request = request;
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
//            System.out.println("channelRead0: " + msg);
            answer.offer(msg);
        }

        String getAnswer(int timeout) throws Exception {
            return answer.poll(timeout, TimeUnit.SECONDS);
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ctx.writeAndFlush(request);
        }
    }

}
