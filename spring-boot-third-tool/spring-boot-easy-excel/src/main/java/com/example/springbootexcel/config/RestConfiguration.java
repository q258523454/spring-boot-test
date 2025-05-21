
package com.example.springbootexcel.config;

import lombok.Data;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * The type Rest configuration.
 *
 * @author yWX907156
 */
@Data
@Configuration
public class RestConfiguration {
    private final int readTimeout = 2000;

    private int connectionTimeout = 2000;

    private int maxTotal = 200;

    private int defaultMaxPerRoute = 100;

    /**
     * Http client factory http components client http request factory.
     *
     * @return HttpComponentsClientHttpRequestFactory http components client http request factory
     */
    @Bean
    public HttpComponentsClientHttpRequestFactory httpClientFactory() {
        HttpComponentsClientHttpRequestFactory factory = new
                HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(connectionTimeout);
        factory.setReadTimeout(readTimeout);
        return factory;
    }

    /**
     * Rest template rest template.
     *
     * @param factory factory
     * @return RestTemplate rest template
     * @throws KeyManagementException   KeyManagementException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws KeyStoreException        KeyStoreException
     */
    @Bean
    public RestTemplate restTemplate(HttpComponentsClientHttpRequestFactory factory)
            throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(null, (x509Certificates, s) -> true);
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(builder.build(),
                new String[]{"TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", new PlainConnectionSocketFactory())
                .register("https", socketFactory).build();
        PoolingHttpClientConnectionManager phccm = new PoolingHttpClientConnectionManager(registry);
        phccm.setMaxTotal(maxTotal);
        phccm.setDefaultMaxPerRoute(defaultMaxPerRoute);
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(socketFactory)
                .setConnectionManager(phccm)
                .setConnectionManagerShared(true)
                .build();
        factory.setHttpClient(httpClient);

        return new RestTemplate(factory);
    }
}