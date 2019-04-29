package com.shiji.png.netty.server.secure;

import com.shiji.png.netty.server.util.JksUtils;

import java.security.KeyStore;

import javax.net.ssl.TrustManagerFactory;

import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;

/**
 * @author bruce.wu
 * @since 2018/12/4 15:07
 */
public class JKSClientSecureStrategy extends ClientSecureStrategy {

    private final SslContext sslCtx;

    public JKSClientSecureStrategy(String host, int port, String jksFile, String jksPassword) throws Exception {
        super(host, port);
        KeyStore keyStore = JksUtils.keyStore(jksFile, jksPassword);
        TrustManagerFactory tmf = JksUtils.trustManagerFactory(keyStore);
        this.sslCtx = SslContextBuilder.forClient()
                .trustManager(tmf)
                .build();
    }

    @Override
    protected SslHandler newSslHandler(ByteBufAllocator allocator) {
        return sslCtx.newHandler(allocator, host, port);
    }
}
