package com.shiji.png.netty.server.boot;

import com.shiji.png.netty.server.config.ServerConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.internal.ConcurrentSet;

/**
 * @author bruce.wu
 * @date 2018/9/21
 */
public class Bootstrap {

    private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    private final Set<ServerConfig> configSet = new LinkedHashSet<>();

    private final Collection<Channel> channels = new ConcurrentSet<>();

    private int numberOfWorkThreads = 0;

    public Bootstrap() {

    }

    public Bootstrap numberOfWorkThreads(int threads) {
        this.numberOfWorkThreads = threads;
        return this;
    }

    public Bootstrap append(ServerConfig config) {
        this.configSet.add(config);
        return this;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(numberOfWorkThreads);

        try {
            for (ServerConfig config : configSet) {
                logger.trace("{}", config);

                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workerGroup);
                b.channel(NioServerSocketChannel.class);
                b.childHandler(new ServerInitializer(config));
                b.option(ChannelOption.SO_BACKLOG, config.getBacklog());

                Channel ch = b.bind(config.getPort()).sync().channel();

                logger.info("server on port {}", config.getPort());

                channels.add(ch);
            }

            for (Channel ch : channels) {
                ch.closeFuture().sync();
            }

        } finally {
            logger.debug("terminated...");
            channels.clear();
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void stop() {
        for (Channel ch : channels) {
            ch.close();
        }
    }

}
