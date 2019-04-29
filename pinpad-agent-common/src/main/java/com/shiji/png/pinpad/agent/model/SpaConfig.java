package com.shiji.png.pinpad.agent.model;

import com.shiji.png.netty.server.codec.CodecStrategy;
import com.shiji.png.netty.server.secure.NonSecureStrategy;
import com.shiji.png.netty.server.secure.SecureStrategy;
import com.shiji.png.pinpad.agent.codec.TwoBytesLengthJsonCodecStrategy;

import lombok.Builder;
import lombok.Getter;

/**
 * @author bruce.wu
 * @since 2018/12/6 9:27
 */
@Getter
@Builder
public class SpaConfig {

    private final String merId;

    private final String terId;

    private final String host;

    private final int port;

    private final int connectTimeout;

    private final int readTimeout;

    private final String moduleName;

    private final String moduleVersion;

    @Builder.Default
    private final SecureStrategy secureStrategy = new NonSecureStrategy();

    @Builder.Default
    private final CodecStrategy codecStrategy = new TwoBytesLengthJsonCodecStrategy(SpaMsg.class);

}
