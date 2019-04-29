package com.shiji.png.netty.server;

import com.shiji.png.netty.server.boot.Bootstrap;
import com.shiji.png.netty.server.config.ServerConfig;

public class ServerRunner {

    private final Bootstrap bootstrap = new Bootstrap();

    public ServerRunner(ServerConfig config) {
        bootstrap.append(config);
    }

    void runInBackground() throws Exception {
        new Thread(() -> {
            try {
                bootstrap.run();
            } catch (Exception e) {
//                System.out.println(e);
            }
        }).start();
        Thread.sleep(100);
    }

    void stop() {
        bootstrap.stop();
    }

}
