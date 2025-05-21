

package com.myjetcache.service.impl;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.myjetcache.service.CreateCacheUseAnnotationService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class CreateCacheUseAnnotationServiceImpl implements CreateCacheUseAnnotationService {
    /**
     * 注解方式
     * local 失效后, 会从 redis 查, redis 也没有则会执行数据库加载器
     */
    @CreateCache(name = "myStringCache:use:annotation:", cacheType = CacheType.BOTH, expire = 3600, localExpire = 5,
            localLimit = 1000)
    @CacheRefresh(refresh = 20, refreshLockTimeout = 10, stopRefreshAfterLastAccess = 3600, timeUnit = TimeUnit.SECONDS)
    private Cache<String, String> stringCache;

    @PostConstruct
    public void init() {
        // 1.cache.get(),如果local和redis都没有缓存, 则会执行数据库加载器 loadFromDb()
        // 2.定时刷新刷新的加载器, 会为每一个key创建刷新任务; 注意: 没有key则不会定时执行数据库加载器, 首次 get/set 都相当于创建了key
        stringCache.config().setLoader(this::loadFromDb);
    }

    /**
     * 刷新执行的加载器,jetcache会为每个key都会创建一个刷新任务, 因此必须设置 stopRefreshAfterLastAccess, 否则内存消耗太大
     *
     * @param key 刷新的key
     */
    public String loadFromDb(String key) {
        // 模拟从数据库读取数据
        String uuid = UUID.randomUUID().toString();
        log.info("[Use Annotation] key:{},load cache form db:{}", key, uuid);
        return uuid;
    }

    @Override
    public Cache<String, String> getStringCache() {
        return stringCache;
    }
}
