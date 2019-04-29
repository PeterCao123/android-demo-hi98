package com.shiji.png.netty.server.secure;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.ssl.PemFileReader;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;

/**
 * PKCS#8 private key stored in PEM format
 * Server X.509 Certificate chain stored in PEM format
 *
 * @author bruce.wu
 * @since 2018/10/23
 */
public class PEMServerSecureStrategy extends ServerSecureStrategy {

    private final SslContext sslCtx;

    public PEMServerSecureStrategy(String certFile, String keyFile) throws Exception {
        this(certFile, keyFile, null);
    }

    public PEMServerSecureStrategy(String certFile, String keyFile, String keyPassword)
            throws Exception {
        X509Certificate[] certificates = PemFileReader.readX509Certificates(certFile);
        PrivateKey privateKey = PemFileReader.readPrivateKey(keyFile, keyPassword);
        this.sslCtx = SslContextBuilder.forServer(privateKey, keyPassword, certificates)
                .build();
    }

    @Override
    protected SslHandler newSslHandler(ByteBufAllocator allocator) {
        return sslCtx.newHandler(allocator);
    }

    @Override
    public String toString() {
        return "PEMServerSecureStrategy";
    }
}
