package io.netty.handler.ssl;

import java.io.File;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

/**
 * @author bruce.wu
 * @since 2018/12/4 13:05
 */
public final class PemFileReader {

    public static PrivateKey readPrivateKey(String keyFile, String keyPassword) throws Exception {
        return readPrivateKey(new File(keyFile), keyPassword);
    }

    public static PrivateKey readPrivateKey(File keyFile, String keyPassword) throws Exception {
        return SslContext.toPrivateKey(keyFile, keyPassword);
    }

    public static PrivateKey readPrivateKey(InputStream keyStream, String keyPassword) throws Exception {
        return SslContext.toPrivateKey(keyStream, keyPassword);
    }

    public static X509Certificate[] readX509Certificates(String file) throws Exception {
        return readX509Certificates(new File(file));
    }

    public static X509Certificate[] readX509Certificates(File file) throws Exception {
        return SslContext.toX509Certificates(file);
    }

    public static X509Certificate[] readX509Certificates(InputStream in) throws Exception {
        return SslContext.toX509Certificates(in);
    }

}
