package com.myjetcache;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
// 开启 Cache
@EnableMethodCache(basePackages = "com.myjetcache")
// 如果不用@CreateCache注解可以删除 EnableCreateCacheAnnotation
@EnableCreateCacheAnnotation
@EnableScheduling
public class SpringJetCacheApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringJetCacheApplication.class, args);
    }
}
