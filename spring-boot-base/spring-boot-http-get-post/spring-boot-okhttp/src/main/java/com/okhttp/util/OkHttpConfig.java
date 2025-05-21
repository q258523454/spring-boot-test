package com.okhttp.util;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;


/**
 * 用@Configuration注解该类，等价与XML中配置beans
 * 用@Bean标注方法等价于XML中配置bean
 */
@Configuration
public class OkHttpConfig {

    private static final Logger logger = LoggerFactory.getLogger(OkHttpUtil.class);

    // 读超时 秒
    @Value("${okhttp3.timeRead:120}")
    private long timeRead = 2 * 60;

    // 写超时 秒
    @Value("${okhttp3.timeWrite:120}")
    private long timeWrite = 2 * 60;

    // 连接超时 秒
    @Value("${okhttp3.timeConnect:120}")
    private long timeConnect = 2 * 60;

    // 长连接时间 分钟
    @Value("${okhttp3.keepAliveDuration:1}")
    private long keepAliveDuration = 1;

    // 线程池最大空闲连接
    @Value("${okhttp3.poolMaxIdleConnections:100}")
    private int poolMaxIdleConnections = 100;

    @PostConstruct
    public void init() {
        logger.info("------------------------------------");
        logger.info("---- okhttp3 timeRead:{}----", timeRead);
        logger.info("---- okhttp3 timeWrite:{}----", timeWrite);
        logger.info("---- okhttp3 timeConnect:{}----", timeConnect);
        logger.info("---- okhttp3 keepAliveDuration:{}----", keepAliveDuration);
        logger.info("---- okhttp3 keepAliveDuration:{}----", poolMaxIdleConnections);
        logger.info("------------------------------------");
    }

    public OkHttpConfig() {
    }

    @Bean
    public X509TrustManager x509TrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    @Bean
    public SSLSocketFactory sslSocketFactory() throws Exception {
        // 信任任何链接
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{x509TrustManager()}, new SecureRandom());
        return sslContext.getSocketFactory();
    }

    @Bean
    public ConnectionPool pool() {
        return new ConnectionPool(poolMaxIdleConnections, keepAliveDuration, TimeUnit.MINUTES);
    }

    @Bean(name = "okHttpClient")
    public OkHttpClient build() throws Exception {
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newBuilder()
                .sslSocketFactory(sslSocketFactory(), x509TrustManager())
                .retryOnConnectionFailure(false) // 关闭自动重连
                .connectionPool(pool())
                .connectTimeout(timeConnect, TimeUnit.SECONDS)
                .writeTimeout(timeWrite, TimeUnit.SECONDS)
                .readTimeout(timeRead, TimeUnit.SECONDS)
                .build();

        return okHttpClient;
    }
}
