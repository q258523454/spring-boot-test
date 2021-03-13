package com.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    private static final int DEFAULT_MAXSIZE = 1000;
    private static final int DEFAULT_TTL = 3600;

    /**
     * 个性化配置缓存
     */
    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager manager = new SimpleCacheManager();
        // 把各个cache注册到cacheManager中，CaffeineCache 实现了org.springframework.cache.Cache接口
        ArrayList<CaffeineCache> caffeineCacheList = new ArrayList<>();
        for (CaffeineConfigEnum c : CaffeineConfigEnum.values()) {
            // 创建不同的 caffeineCache 配置(策略)
            CaffeineCache caffeineCache = new CaffeineCache(c.name(), Caffeine.newBuilder().recordStats()
                    .expireAfterWrite(c.getExpireTime(), TimeUnit.SECONDS)
                    .maximumSize(c.getMaxSize())
                    .build());
            caffeineCacheList.add(caffeineCache);
        }
        manager.setCaches(caffeineCacheList);
        return manager;
    }

    /**
     * 定义cache名称、超时时长秒、最大个数
     * 每个cache缺省3600秒过期，最大个数1000
     */
    public enum CaffeineConfigEnum {
        /**
         * STUDENT: student业务
         * OTHER: 其他业务(默认3600s,最大缓存数:1000
         */
        STUDENT(5, 100),
        OTHER;

        private int maxSize = DEFAULT_MAXSIZE;     // 最大數量
        private int expireTime = DEFAULT_TTL;        // 过期时间（秒）

        CaffeineConfigEnum() {
        }

        CaffeineConfigEnum(int expireTime) {
            this.expireTime = expireTime;
        }

        CaffeineConfigEnum(int expireTime, int maxSize) {
            this.expireTime = expireTime;
            this.maxSize = maxSize;
        }

        public int getMaxSize() {
            return maxSize;
        }

        public void setMaxSize(int maxSize) {
            this.maxSize = maxSize;
        }

        public int getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(int expireTime) {
            this.expireTime = expireTime;
        }
    }

}
