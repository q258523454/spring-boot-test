
package com.myjetcache.service.impl;

import com.alibaba.fastjson.JSON;
import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CachePenetrationProtect;
import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CacheUpdate;
import com.alicp.jetcache.anno.Cached;
import com.myjetcache.entity.Student;
import com.myjetcache.service.JetCacheBaseService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class JetCacheBaseServiceImpl implements JetCacheBaseService {

    private final Random random = new Random();

    /**
     * 增 @Cached
     * 缓存执行的是 com.alicp.jetcache.Cache#PUT() , 接口默认是异步存储
     *
     * @param student spel表达式取值
     * @return 待缓存的的数据（注意,这个返回值不能void）
     */
    @Override
    @Cached(area = "default", name = "my:jetcache:", key = "#student.id", cacheType = CacheType.BOTH,
            expire = 3600, localExpire = 10, timeUnit = TimeUnit.SECONDS, cacheNullValue = false)
    public Student add(Student student) {
        log.info("student:{}", JSON.toJSONString(student));
        return student;
    }

    /**@
     * 删 @CacheInvalidate
     * 方法执行后,再删除,作者 huangli 表示也不会增加这个功能选项, 自主控制粒度即可
     */
    @Override
    @CacheInvalidate(name = "my:jetcache:", key = "#id")
    public void delete(Long id) {
        log.info("delete");
    }

    /**
     * 改 @CacheUpdate
     * 方法执行后,再修改. 作者说明:https://github.com/alibaba/jetcache/issues/115
     */
    @Override
    @CacheUpdate(name = "my:jetcache:", key = "#student.id", value = "#student")
    public void update(Student student) {
        log.info("update:{}", JSON.toJSONString(student));
    }

    /**
     * 查
     * 先查缓存,无缓存则走方法
     */
    @Override
    @Cached(name = "my:jetcache:", key = "#id")
    public Student get(Long id) {
        log.info("load from db");
        // 当缓存不存在, 模拟从数据库查询
        return loadFromDb(id);
    }

    /**
     * 查
     * condition=true: 查缓存, 无缓存则走方法 , condition = "true" 表示一直为true
     * condition=false: 不查缓存, 直接执行方法(查数据库)
     */
    @Override
    @Cached(name = "my:jetcache:", key = "#id", condition = "true")
    public Student get(Long id, boolean isUseCache) {
        // 当 isUseCache 为 false, 或者 缓存不存在的时候, 插叙数据库
        log.info("load from db, isUseCache:{}", isUseCache);
        // 当缓存不存在, 模拟从数据库查询
        return loadFromDb(id);
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
