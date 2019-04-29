package com.shiji.png.netty.server.secure;

import com.shiji.png.netty.server.util.JksUtils;

import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;

/**
 * @author bruce.wu
 * @since 2018/12/4 15:16
 */
public class JKSClientMutualStrategy extends ClientSecureStrategy {

    private final SslContext sslCtx;

    public JKSClientMutualStrategy(String host, int port,
                                   String jksFile, String jksPassword, String keyPassword)
            throws Exception {
        super(host, port);
        KeyStore keyStore = JksUtils.keyStore(jksFile, jksPassword);
        TrustManagerFactory tmf = JksUtils.trustManagerFactory(keyStore);
        KeyManagerFactory kmf = JksUtils.keyManagerFactory(keyStore, keyPassword);
        this.sslCtx = SslContextBuilder.forClient()
                .keyManager(kmf)
                .trustManager(tmf)
                .build();
    }

    @Override
    protected SslHandler newSslHandler(ByteBufAllocator allocator) {
        return sslCtx.newHandler(allocator, host, port);
    }
}
