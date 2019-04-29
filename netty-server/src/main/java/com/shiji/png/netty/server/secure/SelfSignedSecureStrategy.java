package com.shiji.png.netty.server.secure;

import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.ssl.util.SelfSignedCertificate;

/**
 * @author bruce.wu
 * @since 2018/12/7 10:03
 */
public class SelfSignedSecureStrategy extends ServerSecureStrategy {

    private final SslContext sslCtx;

    public SelfSignedSecureStrategy() throws Exception {
        SelfSignedCertificate ssc = new SelfSignedCertificate();
        this.sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
    }

    @Override
    protected SslHandler newSslHandler(ByteBufAllocator allocator) {
        return sslCtx.newHandler(allocator);
    }

}
