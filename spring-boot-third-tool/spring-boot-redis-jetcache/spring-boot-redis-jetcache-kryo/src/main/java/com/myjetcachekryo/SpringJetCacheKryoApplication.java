package com.myjetcachekryo;

import com.alicp.jetcache.anno.config.EnableMethodCache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// 开启 Cache
@EnableMethodCache(basePackages = "com.myjetcachekryo")
public class SpringJetCacheKryoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringJetCacheKryoApplication.class, args);
    }
}
