package com.shiji.png.netty.server;

import com.shiji.png.netty.server.codec.HttpCodecStrategy;
import com.shiji.png.netty.server.config.ServerConfig;
import com.shiji.png.netty.server.handler.HttpHandler;
import com.shiji.png.netty.server.secure.JKSMutualSecureStrategy;
import com.shiji.png.netty.server.secure.JKSServerSecureStrategy;
import com.shiji.png.netty.server.secure.PEMMutualSecureStrategy;
import com.shiji.png.netty.server.secure.PEMServerSecureStrategy;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import static org.junit.Assert.assertEquals;

/**
 * @author bruce.wu
 * @since 2018/12/4 11:58
 */
public class HttpClientTest {

    private static final HttpHandler httpHandler = new HttpHandler();

    @Test
    public void non_secure() throws Exception {
        ServerConfig config = ServerConfig.builder()
                .port(8001)
                .codecStrategy(new HttpCodecStrategy())
                .handler(httpHandler)
                .build();
        ServerRunner runner = new ServerRunner(config);
        runner.runInBackground();
        try {
            HttpGet httpGet = new HttpGet("http://localhost:8001");
            exec(HttpClients.createDefault(), httpGet);
        } finally {
            runner.stop();
        }
    }

    @Test
    public void server_jks() throws Exception {
        ServerConfig config = ServerConfig.builder()
                .port(8002)
                .secureStrategy(new JKSServerSecureStrategy(Certificate.SERVER_JKS_STORE, Certificate.SERVER_JKS_PASSWORD, Certificate.SERVER_KEY_PASSWORD))
                .codecStrategy(new HttpCodecStrategy())
                .handler(httpHandler)
                .build();

        ServerRunner runner = new ServerRunner(config);
        runner.runInBackground();
        try {
            HttpGet httpGet = new HttpGet("https://localhost:8002");
            exec(httpGet);
        } finally {
            runner.stop();
        }
    }

    @Test
    public void mutual_jks() throws Exception {
        ServerConfig config = ServerConfig.builder()
                .port(8003)
                .secureStrategy(new JKSMutualSecureStrategy(Certificate.SERVER_JKS_STORE, Certificate.SERVER_JKS_PASSWORD, Certificate.SERVER_KEY_PASSWORD))
                .codecStrategy(new HttpCodecStrategy())
                .handler(httpHandler)
                .build();

        ServerRunner runner = new ServerRunner(config);
        runner.runInBackground();
        try {
            HttpGet httpGet = new HttpGet("https://localhost:8003");
            exec(httpGet);
        } finally {
            runner.stop();
        }
    }

    @Test
    public void server_pem() throws Exception {
        ServerConfig config = ServerConfig.builder()
                .port(8004)
                .secureStrategy(new PEMServerSecureStrategy(Certificate.SERVER_PEM_CERT_FILE, Certificate.SERVER_PEM_KEY_FILE, Certificate.SERVER_PEM_KEY_PASSWORD))
                .codecStrategy(new HttpCodecStrategy())
                .handler(httpHandler)
                .build();

        ServerRunner runner = new ServerRunner(config);
        runner.runInBackground();
        try {
            HttpGet httpGet = new HttpGet("https://localhost:8004");
            exec(httpGet);
        } finally {
            runner.stop();
        }
    }

    @Test
    public void mutual_pem() throws Exception {
        ServerConfig config = ServerConfig.builder()
                .port(8005)
                .secureStrategy(new PEMMutualSecureStrategy(Certificate.SERVER_PEM_CERT_FILE, Certificate.SERVER_PEM_KEY_FILE, Certificate.SERVER_PEM_KEY_PASSWORD, Certificate.SERVER_PEM_TRUST_FILE))
                .codecStrategy(new HttpCodecStrategy())
                .handler(httpHandler)
                .build();

        ServerRunner runner = new ServerRunner(config);
        runner.runInBackground();
        try {
            HttpGet httpGet = new HttpGet("https://localhost:8005");
            exec(httpGet);
        } finally {
            runner.stop();
        }
    }

    private CloseableHttpClient client() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (InputStream is = new FileInputStream(Certificate.CLIENT_JKS_STORE)) {
            keyStore.load(is, Certificate.CLIENT_JKS_PASSWORD.toCharArray());
        }

        SSLContext sslContext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, Certificate.CLIENT_KEY_PASSWORD.toCharArray())
                .loadTrustMaterial(keyStore, new TrustSelfSignedStrategy())
                .build();

        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
                sslContext,
                new String[] { "TLSv1.2" },
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());

        return HttpClients.custom()
                .setSSLSocketFactory(socketFactory)
                .build();
    }

    private void exec(CloseableHttpClient client, HttpGet httpGet) throws Exception {
        CloseableHttpResponse response = client.execute(httpGet);
        try {
//            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            String body = EntityUtils.toString(entity);
//            System.out.println(body);
            assertEquals("Hello World", body);
        } finally {
            client.close();
            response.close();
        }
    }

    private void exec(HttpGet httpGet) throws Exception {
        exec(client(), httpGet);
    }

}
