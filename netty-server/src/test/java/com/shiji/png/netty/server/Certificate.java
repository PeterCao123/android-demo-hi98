package com.shiji.png.netty.server;

/**
 * @author bruce.wu
 * @since 2018/12/4 11:41
 */
public interface Certificate {

    /**
     * server jks file
     * include server.crt, server.key
     * trusts: client.crt
     */
    String SERVER_JKS_STORE = "src/test/resources/.jks/server.jks";
    String SERVER_JKS_PASSWORD = "storepass";
    String SERVER_KEY_PASSWORD = "keypass";

    /**
     * client jks file
     * include client.crt, client.key
     * trusts: server.crt, ca.crt
     */
    String CLIENT_JKS_STORE = "src/test/resources/.jks/client.jks";
    String CLIENT_JKS_PASSWORD = "storepass";
    String CLIENT_KEY_PASSWORD = "keypass";

    /**
     * server pem files
     * include server.crt, server.pk8.key
     * trusts: client.crt
     */
    String SERVER_PEM_CERT_FILE = "src/test/resources/.pem/server.crt";
    String SERVER_PEM_KEY_FILE = "src/test/resources/.pem/server.pk8.key";
    String SERVER_PEM_KEY_PASSWORD = null;
    String SERVER_PEM_TRUST_FILE = "src/test/resources/.jks/client.crt";

}
