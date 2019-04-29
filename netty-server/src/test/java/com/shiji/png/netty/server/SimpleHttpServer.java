package com.shiji.png.netty.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.PemFileReader;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

/**
 * @author bruce.wu
 * @since 2019/1/15 9:07
 */
public class SimpleHttpServer {

    public static SimpleHttpServer fromJKS(String jksFile, String jksPassword, String keyPassword)
            throws IOException, GeneralSecurityException {
        try (InputStream is = new FileInputStream(jksFile)) {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(is, jksPassword == null ? null : jksPassword.toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, keyPassword == null ? null : keyPassword.toCharArray());

            SslContext sslCtx = SslContextBuilder.forServer(kmf).build();

            return new SimpleHttpServer(sslCtx);
        }
    }

    public static SimpleHttpServer fromPEM(String crtFile, String keyFile) throws Exception {
        return fromPEM(crtFile, keyFile, null);
    }

    public static SimpleHttpServer fromPEM(String crtFile, String keyFile, String keyPassword) throws Exception {
        X509Certificate[] certificates = PemFileReader.readX509Certificates(crtFile);
        PrivateKey privateKey = PemFileReader.readPrivateKey(keyFile, keyPassword);
        SslContext sslCtx = SslContextBuilder.forServer(privateKey, keyPassword, certificates).build();
        return new SimpleHttpServer(sslCtx);
    }

    private final SslContext sslCtx;

    private int port;
    private int backlog;
    private SimpleChannelInboundHandler<HttpRequest> handler;

    private boolean logging = false;
    private LogLevel logLevel;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private ChannelFuture future;

    private SimpleHttpServer() {
        this(null);
    }

    private SimpleHttpServer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
        this.port = sslCtx == null ? 80 : 443;
        this.backlog = 16;
    }

    public SimpleHttpServer setPort(int port) {
        this.port = port;
        return this;
    }

    public int getPort() {
        return this.port;
    }

    public SimpleHttpServer setBacklog(int backlog) {
        this.backlog = backlog;
        return this;
    }

    public int getBacklog() {
        return this.backlog;
    }

    public SimpleHttpServer setHandler(SimpleChannelInboundHandler<HttpRequest> handler) {
        this.handler = handler;
        return this;
    }

    public SimpleHttpServer enableLogging() {
        return enableLogging(LogLevel.TRACE);
    }

    public SimpleHttpServer enableLogging(LogLevel logLevel) {
        this.logging = true;
        this.logLevel = logLevel;
        return this;
    }

    public SimpleHttpServer start() {
        if (handler == null) {
            throw new NullPointerException("handler is null");
        }

        this.bossGroup = new NioEventLoopGroup(1);
        this.workerGroup = new NioEventLoopGroup();

        this.future = new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, backlog)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        if (sslCtx != null) {
                            pipeline.addLast(sslCtx.newHandler(ch.alloc()));
                        }
                        pipeline.addLast(new HttpServerCodec());
                        pipeline.addLast(new HttpServerExpectContinueHandler());
                        if (logging) {
                            pipeline.addLast(new LoggingHandler(logLevel));
                        }
                        pipeline.addLast(handler);
                    }
                })
                .bind(port)
                .syncUninterruptibly();
        return this;
    }

    public void sync() {
        this.future.syncUninterruptibly();
    }

    public void shutdown() {
        future.channel().close();
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

}
