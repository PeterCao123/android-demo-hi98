package com.shiji.png.pat.spat.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

/**
 * @author bruce.wu
 * @since 2018/12/13 17:48
 */
public class StrongSslSocketFactory extends SSLSocketFactory {

    private static final Logger logger = LoggerFactory.getLogger("StrongSslSocketFactory");

    private static final String TLS_V1 = "TLSv1";
    private static final String TLS_V11 = "TLSv1.1";
    private static final String TLS_V12 = "TLSv1.2";

    private static final String[] TLS_ONLY = new String[]{TLS_V1, TLS_V11, TLS_V12};

    public static final String[] DEFAULT_PROTOCOLS = new String[]{TLS_V11, TLS_V12};

    public static SSLSocketFactory create() {
        return create(getDefaultSslSocketFactory(null, null));
    }

    public static SSLSocketFactory create(String[] supportedProtocols) {
        return create(getDefaultSslSocketFactory(null, null), supportedProtocols);
    }

    public static SSLSocketFactory create(KeyManager[] km, TrustManager[] tm) {
        return create(getDefaultSslSocketFactory(km, tm));
    }

    public static SSLSocketFactory getDefaultSslSocketFactory(KeyManager[] km, TrustManager[] tm) {
        final SSLContext sslCtx = getSslContext();
        try {
            sslCtx.init(km, tm, new SecureRandom());
        } catch (KeyManagementException e) {
            logger.error("init default SSLSocketFactory error", e);
            throw new IllegalStateException(e);
        }
        return sslCtx.getSocketFactory();
    }

    private static SSLContext getSslContext() {
        try {
            return SSLContext.getInstance(TLS_V12);
        } catch (NoSuchAlgorithmException ignore) {
            try {
                return SSLContext.getInstance("TLS");
            } catch (NoSuchAlgorithmException e) {
                logger.error("no TLS provider", e);
                throw new IllegalStateException("No TLS provider", e);
            }
        }
    }

    public static SSLSocketFactory create(SSLSocketFactory delegate) {
        return create(delegate, DEFAULT_PROTOCOLS);
    }

    public static SSLSocketFactory create(SSLSocketFactory delegate, String[] supportedProtocols) {
        return new StrongSslSocketFactory(delegate, supportedProtocols, null);
    }

    private final SSLSocketFactory delegate;
    private final String[] supportedProtocols;
    private final String[] supportedCipherSuites;

    private StrongSslSocketFactory(SSLSocketFactory delegate,
                                   String[] supportedProtocols,
                                   String[] supportedCipherSuites) {
        this.delegate = delegate;
        this.supportedProtocols = supportedProtocols;
        this.supportedCipherSuites = supportedCipherSuites;
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return delegate.getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return delegate.getSupportedCipherSuites();
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
        return customizeSocket(delegate.createSocket(socket, host, port, autoClose));
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        return customizeSocket(delegate.createSocket(host, port));
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localAddress, int localPort) throws IOException, UnknownHostException {
        return customizeSocket(delegate.createSocket(host, port, localAddress, localPort));
    }

    @Override
    public Socket createSocket(InetAddress address, int port) throws IOException {
        return customizeSocket(delegate.createSocket(address, port));
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        return customizeSocket(delegate.createSocket(address, port, localAddress, localPort));
    }

    private Socket customizeSocket(Socket socket) {
        if (!(socket instanceof SSLSocket)) {
            return socket;
        }

        final SSLSocket sslSocket = (SSLSocket)socket;

        if (supportedCipherSuites != null) {
            sslSocket.setEnabledCipherSuites(supportedCipherSuites);
        }

        if (supportedProtocols != null) {
            if (logger.isDebugEnabled()) {
                logger.info("customized supported protocols: {}", Arrays.toString(supportedProtocols));
            }
            sslSocket.setEnabledProtocols(supportedProtocols);
            return socket;
        }

        final String[] allProtocols = sslSocket.getSupportedProtocols();
        if (logger.isDebugEnabled()) {
            logger.info("all enabled protocols: {}", Arrays.toString(allProtocols));
        }

        final List<String> enabledProtocols = new ArrayList<>(allProtocols.length);
        for (final String protocol : allProtocols) {
            if (!protocol.startsWith("SSL")) {
                enabledProtocols.add(protocol);
            }
        }
        if (!enabledProtocols.isEmpty()) {
            if (logger.isDebugEnabled()) {
                logger.info("customized enabled protocols: {}", enabledProtocols);
            }
            sslSocket.setEnabledProtocols(enabledProtocols.toArray(new String[0]));
        } else {
            if (logger.isDebugEnabled()) {
                logger.info("default enabled protocols: {}", Arrays.toString(allProtocols));
            }
        }

        return socket;
    }

}
