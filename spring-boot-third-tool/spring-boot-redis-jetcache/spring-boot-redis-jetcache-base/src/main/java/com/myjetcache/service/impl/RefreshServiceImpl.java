

package com.myjetcache.service.impl;

import com.alibaba.fastjson.JSON;
import com.alicp.jetcache.anno.CachePenetrationProtect;
import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.myjetcache.entity.Student;
import com.myjetcache.service.RefreshService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class RefreshServiceImpl implements RefreshService {

    private final Random random = new Random();

    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    /**
     * 注意 CacheRefresh 会为每个key创建定时任务, 定时来执行这个方法
     * 官方文档:
     * 1.目的是为了防止缓存失效时造成的雪崩效应打爆数据库
     * 2.对key比较少,实时性要求不高,加载开销非常大的缓存场景,适合使用自动刷新
     * <p>
     * CacheRefresh 刷新机制:
     * 1.如果 CacheType.LOCAL ,那么多个节点会重复刷新。
     * 2.如果 CacheType.REMOTE ,通过在远程缓存中的分布式锁'_#TS#',保证一个周期内只有一个节点执行了刷新操作.
     * 3.如果 CacheType.BOTH ,即两级缓存,通过在远程缓存中的分布式锁,保证一个周期内只有一个节点执行了刷新操作.
     * 注意:仅更新REMOTE,其节点的本地缓存不会更新.jetcache支持给远程和本地缓存设置不同的超时时间，所以可以把本地缓存的超时时间设置短一点.
     * <p>
     * CachePenetrationProtect 注解:
     * 当缓存访问【未命中】的情况下,对并发进行的加载行为进行保护.
     * 当前版本实现的是单JVM内的保护，即同一个JVM中同一个key只有一个线程去加载，其它线程等待结果
     * <p>
     * 调用接口时, 有缓存则返回缓存值，没有则执行方法后再缓存。
     *
     * @param key 缓存的 key
     * @return 缓存的 value
     */
    @Override
    @Cached(name = "my:refresh:stringCache.", key = "#key", cacheNullValue = true, cacheType = CacheType.BOTH,
            expire = 10, localExpire = 5, timeUnit = TimeUnit.SECONDS)
    @CacheRefresh(timeUnit = TimeUnit.SECONDS, refresh = 15, refreshLockTimeout = 10, stopRefreshAfterLastAccess = 600)
    @CachePenetrationProtect
    public String getRefreshStringCache(String key) {
        String value;
        // 模拟查询数据库, 首次查询返回 null, 以后每次查询,如果缓存不存在,则返回 随机uuid
        if (atomicInteger.get() == 0) {
            atomicInteger.addAndGet(1);
            value = null;
        } else {
            // 每隔15秒就会刷新新的uuid到本地缓存和redis
            value = UUID.randomUUID().toString();
        }
        log.info("return value:{}", value);
        return value;
    }


    /**
     * Map结构的SpEL表达式参考下面的写法
     * 入参是的 SpEL是 key
     * return 是存储的 value
     */
    @Override
    @Cached(area = "default", name = "my:jetcache:", key = "#obj['id']", cacheType = CacheType.BOTH,
            expire = 3600, localExpire = 60, timeUnit = TimeUnit.SECONDS, cacheNullValue = false)
    @CacheRefresh(timeUnit = TimeUnit.SECONDS, refresh = 10, refreshLockTimeout = 10, stopRefreshAfterLastAccess = 3600)
    @CachePenetrationProtect
    public Student getRefreshMapCache(Object obj) {
        log.info("obj:{}", JSON.toJSONString(obj));
        Student student = Student.getStudent(String.valueOf(random.nextInt(100)));
        log.info("after refresh:{}", JSON.toJSONString(student));
        return student;
    }
}
