package com.myjetcache.service.impl;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheManager;
import com.alicp.jetcache.RefreshPolicy;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.template.QuickConfig;
import com.myjetcache.service.CreateCacheUseMethodService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class CreateCacheUseMethodServiceImpl implements CreateCacheUseMethodService {

    @Autowired
    private CacheManager cacheManager;

    private Cache<String, String> stringMethodCache;

    @PostConstruct
    public void init() {
        // 注意必须要refreshPolicy() ,才能创建 RefreshCache, 否则自动刷新功能无效
        QuickConfig quickConfigStringCache = QuickConfig.newBuilder("myStringCache:use:method:") // 缓存的前缀
                .cacheType(CacheType.BOTH) // local 和 remote 组合成两级缓存
                .expire(Duration.ofSeconds(3600)) // 远程过期时间
                .localExpire(Duration.ofSeconds(5)) // 本地过期时间, 应该小于远程过期时间, 只对CacheType.LOCAL和CacheType.BOTH有效
                .localLimit(1000) // 本地缓存的最大元素数量, 默认:100
                .cacheNullValue(false) // 是否缓存 NULL 值
                .refreshPolicy(getRefreshPolicy()) // 这里必须显式创建, 不要使用 cache.config().setRefreshPolicy(), 否则无效
                .build();

        stringMethodCache = cacheManager.getOrCreateCache(quickConfigStringCache);
        // 刷新执行的加载器, 会遍历刷新每一个key
        stringMethodCache.config().setLoader(this::loadFromDb);
    }

    /**
     * 创建刷新策略
     * 等于注解:@CacheRefresh(refresh = 10, refreshLockTimeout = 10, stopRefreshAfterLastAccess = 3600, timeUnit = TimeUnit.SECONDS)
     */
    private RefreshPolicy getRefreshPolicy() {
        RefreshPolicy refreshPolicy = new RefreshPolicy();
        // 刷新时间间隔
        refreshPolicy.setRefreshMillis(10 * 1000L);
        // 类型为 BOTH/REMOTE 的缓存刷新时，同时只会有一台服务器在刷新，这台服务器会在远程缓存放置一个分布式锁，此配置指定该锁的超时时间
        // 不管有多少台服务器,同时只有一个服务器在刷新,这是通过 tryLock 实现的
        refreshPolicy.setRefreshLockTimeoutMillis(10 * 1000);
        // 指定多久未访问后停止自动刷新。 注意:不指定则会一直刷新
        refreshPolicy.setStopRefreshAfterLastAccessMillis(3600 * 1000);
        return refreshPolicy;
    }

    /**
     * 刷新执行的加载器
     * 每个key都有一个刷新任务, 因此必须设置 stopRefreshAfterLastAccess
     * 注意: 没有key则不会定时执行数据库加载器, 首次 get/set 都相当于创建了key
     *
     * @param key 刷新的key
     */
    public String loadFromDb(String key) {
        // 模拟从数据库读取数据
        String uuid = UUID.randomUUID().toString();
        log.info("[Use Method] key:{},load cache form db:{}", key, uuid);
        return uuid;
    }

    @Override
    public Cache<String, String> getStringMethodCache() {
        return stringMethodCache;
    }
}
