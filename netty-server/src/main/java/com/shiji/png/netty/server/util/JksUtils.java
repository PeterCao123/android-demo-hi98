package com.shiji.png.netty.server.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * @author bruce.wu
 * @since 2018/12/4 15:11
 */
public final class JksUtils {

    public static KeyStore keyStore(String jksFile, String jksPassword) throws Exception {
        try (InputStream is = new FileInputStream(jksFile)) {
            return keyStore(is, jksPassword);
        }
    }

    public static KeyStore keyStore(InputStream jksStream, String jksPassword) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(jksStream, jksPassword.toCharArray());
        return keyStore;
    }

    public static KeyManagerFactory keyManagerFactory(KeyStore keyStore, String keyPassword) throws Exception {
        KeyManagerFactory factory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        factory.init(keyStore, keyPassword.toCharArray());
        return factory;
    }

    public static TrustManagerFactory trustManagerFactory(KeyStore keyStore) throws Exception {
        final TrustManagerFactory factory = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        factory.init(keyStore);
        return factory;
    }

}
