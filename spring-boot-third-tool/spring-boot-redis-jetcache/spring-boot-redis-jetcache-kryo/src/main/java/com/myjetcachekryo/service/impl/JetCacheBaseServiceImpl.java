
package com.myjetcachekryo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alicp.jetcache.anno.CachePenetrationProtect;
import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.myjetcachekryo.entity.Student;
import com.myjetcachekryo.service.JetCacheBaseService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class JetCacheBaseServiceImpl implements JetCacheBaseService {

    private final Random random = new Random();

    private final AtomicInteger atomicInteger = new AtomicInteger(0);


    @Override
    @Cached(area = "default", name = "my:jetcache:", key = "#student.id", cacheType = CacheType.BOTH,
            expire = 3600, localExpire = 10, timeUnit = TimeUnit.SECONDS, cacheNullValue = false)
    public Student add(Student student) {
        log.info("student:{}", JSON.toJSONString(student));
        return student;
    }


    @Override
    @Cached(area = "default", name = "my:jetcache:", key = "#id", condition = "#isUseCache==true")
    public Student get(Long id, boolean isUseCache) {
        // 当 isUseCache 为 false, 或者 缓存不存在的时候, 插叙数据库
        log.info("load from db, isUseCache:{}", isUseCache);
        // 当缓存不存在, 模拟从数据库查询
        return loadFromDb(id);
    }


    @Override
    @Cached(area = "default", name = "my:jetcache.", key = "#id", cacheNullValue = true, cacheType = CacheType.BOTH,
            expire = 3600, localExpire = 60, timeUnit = TimeUnit.SECONDS)
    @CacheRefresh(timeUnit = TimeUnit.SECONDS, refresh = 10, refreshLockTimeout = 10, stopRefreshAfterLastAccess = 3600)
    @CachePenetrationProtect
    public Student getRefresh(Long id) {
        Student value;
        // 模拟查询数据库, 首次查询返回 null, 以后每次查询,如果缓存不存在,则返回 随机uuid
        if (atomicInteger.get() == 0) {
            atomicInteger.addAndGet(1);
            value = null;
        } else {
            // 每隔10秒就会刷新新的uuid到 本地和redis
            value = loadFromDb(id);
        }
        log.info("return value:{}", JSON.toJSONString(value));
        return value;
    }

    /**
     * 模拟从数据库查询
     */
    private Student loadFromDb(Long id) {
        Student student = new Student();
        student.setId(Math.toIntExact(id));
        student.setName("load from db");
        student.setAge(random.nextInt(100));
        return student;
    }
}
