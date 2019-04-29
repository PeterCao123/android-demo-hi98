package com.shiji.png.netty.server.secure;

import com.shiji.png.netty.server.util.JksUtils;

import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManagerFactory;

import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;

/**
 * Server private key, x509 certificate and trusted client certificate chain stored in jks file
 *
 * @author bruce.wu
 * @since 2018/10/23
 */
public class JKSMutualSecureStrategy extends ServerSecureStrategy {

    private final SslContext sslCtx;

    public JKSMutualSecureStrategy(String jksFile, String jksPassword, String keyPassword)
            throws Exception {
        KeyStore keyStore = JksUtils.keyStore(jksFile, jksPassword);
        KeyManagerFactory kmf = JksUtils.keyManagerFactory(keyStore, keyPassword);
        TrustManagerFactory tmf = JksUtils.trustManagerFactory(keyStore);
        this.sslCtx = SslContextBuilder.forServer(kmf).trustManager(tmf).build();
    }

    @Override
    protected SslHandler newSslHandler(ByteBufAllocator allocator) {
        SSLEngine engine = sslCtx.newEngine(allocator);
        engine.setUseClientMode(false);
        engine.setNeedClientAuth(true);
        return new SslHandler(engine);
    }

    @Override
    public String toString() {
        return "JKSMutualSecureStrategy";
    }
}
