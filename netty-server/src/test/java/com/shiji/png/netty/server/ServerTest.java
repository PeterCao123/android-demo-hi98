package com.shiji.png.netty.server;

import com.shiji.png.netty.server.boot.Bootstrap;
import com.shiji.png.netty.server.codec.HttpCodecStrategy;
import com.shiji.png.netty.server.codec.TwoBytesLengthTextCodecStrategy;
import com.shiji.png.netty.server.config.ServerConfig;
import com.shiji.png.netty.server.handler.EchoHandler;
import com.shiji.png.netty.server.handler.HttpHandler;
import com.shiji.png.netty.server.secure.JKSMutualSecureStrategy;
import com.shiji.png.netty.server.secure.JKSServerSecureStrategy;
import com.shiji.png.netty.server.secure.PEMMutualSecureStrategy;
import com.shiji.png.netty.server.secure.PEMServerSecureStrategy;

/**
 * @author bruce.wu
 * @since 2018/12/4 11:05
 */
public class ServerTest {

    public static void main(String[] args) throws Exception {
        HttpHandler httpHandler = new HttpHandler();
        EchoHandler echoHandler = new EchoHandler();

        ServerConfig httpConfig1 = ServerConfig.builder()
                .port(8001)
                .codecStrategy(new HttpCodecStrategy())
                .handler(httpHandler)
                .build();
        ServerConfig httpConfig2 = ServerConfig.builder()
                .port(8002)
                .secureStrategy(new JKSServerSecureStrategy(Certificate.SERVER_JKS_STORE, Certificate.SERVER_JKS_PASSWORD, Certificate.SERVER_KEY_PASSWORD))
                .codecStrategy(new HttpCodecStrategy())
                .handler(httpHandler)
                .build();
        ServerConfig httpConfig3 = ServerConfig.builder()
                .port(8003)
                .secureStrategy(new JKSMutualSecureStrategy(Certificate.SERVER_JKS_STORE, Certificate.SERVER_JKS_PASSWORD, Certificate.SERVER_KEY_PASSWORD))
                .codecStrategy(new HttpCodecStrategy())
                .handler(httpHandler)
                .build();
        ServerConfig httpConfig4 = ServerConfig.builder()
                .port(8004)
                .secureStrategy(new PEMServerSecureStrategy(Certificate.SERVER_PEM_CERT_FILE, Certificate.SERVER_PEM_KEY_FILE, Certificate.SERVER_PEM_KEY_PASSWORD))
                .codecStrategy(new HttpCodecStrategy())
                .handler(httpHandler)
                .build();
        ServerConfig httpConfig5 = ServerConfig.builder()
                .port(8005)
                .secureStrategy(new PEMMutualSecureStrategy(Certificate.SERVER_PEM_CERT_FILE, Certificate.SERVER_PEM_KEY_FILE, Certificate.SERVER_PEM_KEY_PASSWORD, Certificate.SERVER_PEM_TRUST_FILE))
                .codecStrategy(new HttpCodecStrategy())
                .handler(httpHandler)
                .build();

        ServerConfig echoConfig1 = ServerConfig.builder()
                .port(8101)
                .codecStrategy(new TwoBytesLengthTextCodecStrategy())
                .handler(echoHandler)
                .build();
        ServerConfig echoConfig2 = ServerConfig.builder()
                .port(8102)
                .secureStrategy(new JKSServerSecureStrategy(Certificate.SERVER_JKS_STORE, Certificate.SERVER_JKS_PASSWORD, Certificate.SERVER_KEY_PASSWORD))
                .codecStrategy(new TwoBytesLengthTextCodecStrategy())
                .handler(echoHandler)
                .build();
        ServerConfig echoConfig3 = ServerConfig.builder()
                .port(8103)
                .secureStrategy(new JKSMutualSecureStrategy(Certificate.SERVER_JKS_STORE, Certificate.SERVER_JKS_PASSWORD, Certificate.SERVER_KEY_PASSWORD))
                .codecStrategy(new TwoBytesLengthTextCodecStrategy())
                .handler(echoHandler)
                .build();
        ServerConfig echoConfig4 = ServerConfig.builder()
                .port(8104)
                .secureStrategy(new PEMServerSecureStrategy(Certificate.SERVER_PEM_CERT_FILE, Certificate.SERVER_PEM_KEY_FILE, Certificate.SERVER_PEM_KEY_PASSWORD))
                .codecStrategy(new TwoBytesLengthTextCodecStrategy())
                .handler(echoHandler)
                .build();
        ServerConfig echoConfig5 = ServerConfig.builder()
                .port(8105)
                .secureStrategy(new PEMMutualSecureStrategy(Certificate.SERVER_PEM_CERT_FILE, Certificate.SERVER_PEM_KEY_FILE, Certificate.SERVER_PEM_KEY_PASSWORD, Certificate.SERVER_PEM_TRUST_FILE))
                .codecStrategy(new TwoBytesLengthTextCodecStrategy())
                .handler(echoHandler)
                .build();

        new Bootstrap()
                .append(httpConfig1).append(httpConfig2).append(httpConfig3).append(httpConfig4).append(httpConfig5)
                .append(echoConfig1).append(echoConfig2).append(echoConfig3).append(echoConfig4).append(echoConfig5)
                .run();
    }

}
