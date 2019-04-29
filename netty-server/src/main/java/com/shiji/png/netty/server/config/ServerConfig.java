package com.shiji.png.netty.server.config;

import com.shiji.png.netty.server.codec.CodecStrategy;
import com.shiji.png.netty.server.codec.TwoBytesLengthTextCodecStrategy;
import com.shiji.png.netty.server.secure.NonSecureStrategy;
import com.shiji.png.netty.server.secure.SecureStrategy;

import io.netty.channel.ChannelHandler;

/**
 * @author bruce.wu
 * @date 2018/10/19
 */
public final class ServerConfig {

    public static Builder builder() {
        return new Builder();
    }

    private final int port;
    private final int backlog;
    private final SecureStrategy secureStrategy;
    private final CodecStrategy codecStrategy;
    private final ChannelHandler handler;

    private ServerConfig(int port,
                         int backlog,
                         SecureStrategy secureStrategy,
                         CodecStrategy codecStrategy,
                         ChannelHandler handler) {
        this.port = port;
        this.backlog = backlog;
        this.secureStrategy = secureStrategy;
        this.codecStrategy = codecStrategy;
        this.handler = handler;
    }

    public int getPort() {
        return port;
    }

    public int getBacklog() {
        return backlog;
    }

    public SecureStrategy getSecureStrategy() {
        return secureStrategy;
    }

    public CodecStrategy getCodecStrategy() {
        return codecStrategy;
    }

    public ChannelHandler getHandler() {
        return handler;
    }

    @Override
    public String toString() {
        return "ServerConfig{" +
                "port=" + port +
                ", backlog=" + backlog +
                ", secureStrategy=" + secureStrategy +
                ", codecStrategy=" + codecStrategy +
                ", handler=" + handler.getClass().getSimpleName() +
                '}';
    }

    public static class Builder {

        private static final int DEFAULT_BACKLOG = 8;
        private static final SecureStrategy DEFAULT_SECURE_STRATEGY = new NonSecureStrategy();
        private static final CodecStrategy DEFAULT_CODEC_STRATEGY = new TwoBytesLengthTextCodecStrategy();

        private int port;
        private int backlog = DEFAULT_BACKLOG;
        private SecureStrategy secureStrategy = DEFAULT_SECURE_STRATEGY;
        private CodecStrategy codecStrategy = DEFAULT_CODEC_STRATEGY;
        private ChannelHandler handler;

        private Builder() {}

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder backlog(int backlog) {
            this.backlog = backlog;
            return this;
        }

        public Builder secureStrategy(SecureStrategy strategy) {
            this.secureStrategy = strategy;
            return this;
        }

        public Builder codecStrategy(CodecStrategy strategy) {
            this.codecStrategy = strategy;
            return this;
        }

        public Builder handler(Class<? extends ChannelHandler> handler) throws ReflectiveOperationException {
            this.handler = handler.newInstance();
            return this;
        }

        public Builder handler(ChannelHandler handler) {
            this.handler = handler;
            return this;
        }

        public ServerConfig build() {
            return new ServerConfig(port, backlog, secureStrategy, codecStrategy, handler);
        }

    }

}
