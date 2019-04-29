package com.shiji.png.netty.server.secure;

import com.shiji.png.netty.server.util.JksUtils;

import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;

import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;

/**
 * Server private key and x509 certificate chain stored in jks file
 *
 * @author bruce.wu
 * @since 2018/9/25
 */
public class JKSServerSecureStrategy extends ServerSecureStrategy {

    private final SslContext sslCtx;

    public JKSServerSecureStrategy(String jksFile, String jksPassword, String keyPassword)
            throws Exception {
        KeyStore keyStore = JksUtils.keyStore(jksFile, jksPassword);
        KeyManagerFactory kmf = JksUtils.keyManagerFactory(keyStore, keyPassword);
        this.sslCtx = SslContextBuilder.forServer(kmf).build();
    }

    @Override
    protected SslHandler newSslHandler(ByteBufAllocator allocator) {
        return sslCtx.newHandler(allocator);
    }

    @Override
    public String toString() {
        return "JKSServerSecureStrategy";
    }

}
