package com.shiji.png.netty.server.secure;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLEngine;

import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.ssl.PemFileReader;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;

/**
 * @author bruce.wu
 * @since 2018/10/23
 */
public class PEMMutualSecureStrategy extends ServerSecureStrategy {

    private final SslContext sslCtx;

    public PEMMutualSecureStrategy(String certFile, String keyFile, String trustsFile) throws Exception {
        this(certFile, keyFile, null, trustsFile);
    }

    public PEMMutualSecureStrategy(String certFile, String keyFile, String keyPassword, String trustsFile)
            throws Exception {
        X509Certificate[] certificates = PemFileReader.readX509Certificates(certFile);
        PrivateKey privateKey = PemFileReader.readPrivateKey(keyFile, keyPassword);
        X509Certificate[] trusts = PemFileReader.readX509Certificates(trustsFile);
        this.sslCtx = SslContextBuilder.forServer(privateKey, keyPassword, certificates)
                .trustManager(trusts)
                .build();
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
        return "PEMMutualSecureStrategy";
    }

}
