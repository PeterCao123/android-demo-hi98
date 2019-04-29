package com.shiji.png.netty.server;

import com.shiji.png.netty.server.handler.HttpHandler;

/**
 * @author bruce.wu
 * @since 2019/1/15 9:43
 */
public class SimpleHttpServerApp {

    public static void main(String[] args) throws Exception {
        HttpHandler handler = new HttpHandler();
        SimpleHttpServer server = forJKS()
                .setHandler(handler)
                .enableLogging()
                .start();
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
        server.sync();
    }

    public static SimpleHttpServer forJKS() throws Exception {
        return SimpleHttpServer.fromJKS(Certificate.SERVER_JKS_STORE,
                Certificate.SERVER_JKS_PASSWORD,
                Certificate.SERVER_KEY_PASSWORD);
    }

    public static SimpleHttpServer forPEM() throws Exception {
        return SimpleHttpServer.fromPEM(Certificate.SERVER_PEM_CERT_FILE,
                Certificate.SERVER_PEM_KEY_FILE,
                Certificate.SERVER_PEM_KEY_PASSWORD);
    }

}
