package com.myjetcachekryo;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.alicp.jetcache.support.DecoderMap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
// 开启 Cache
@EnableMethodCache(basePackages = "com.myjetcachekryo")
public class SpringJetCacheKryoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringJetCacheKryoApplication.class, args);
    }
}
