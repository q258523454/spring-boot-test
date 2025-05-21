package com.sec.util;

import com.sec.redis.abstracts.AbstractRedisKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @date 2021-08-04 19:19
 * @modify
 */
@Slf4j
public enum RedisUtil {
    ;

    public static StringRedisTemplate getRedisTemplate() {
        return (StringRedisTemplate) SpringContextHolder.getBean("stringRedisTemplate");
    }

    /**
     * 设置指定 key 的值
     */
    public static void set(String key, String value) {
        getRedisTemplate().opsForValue().set(key, value);
    }

    /**
     * 设置key,value
     * @param prefix 抽象前缀,包含了 前缀和过期时间
     */
    public static void set(AbstractRedisKey prefix, String key, String value) {
        String realKey = prefix.getPrefix() + ":" + key;
        int expireSeconds = prefix.getExpireSeconds();
        if (expireSeconds > 0) {
            setEx(realKey, value, expireSeconds, TimeUnit.SECONDS);
        } else {
            set(realKey, value);
        }
    }

    /**
     * 将值 value 关联到 key ，并将 key 的过期时间设为 timeout
     * @param timeout 过期时间
     * @param unit    时间单位
     */
    public static void setEx(String key, String value, long timeout, TimeUnit unit) {
        getRedisTemplate().opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 获取指定 key 的值
     */
    public static String get(String key) {
        return getRedisTemplate().opsForValue().get(key);
    }


    /**
     * 获取指定 key 的值
     */
    public static String get(AbstractRedisKey prefix, String key) {
        String realKey = prefix.getPrefix() + ":" + key;
        return getRedisTemplate().opsForValue().get(realKey);
    }


    /**
     * 是否存在key
     */
    public static Boolean hasKey(String key) {
        return getRedisTemplate().hasKey(key);
    }

    /**
     * 是否存在key
     */
    public static Boolean hasKey(AbstractRedisKey prefix,String key) {
        String realKey = prefix.getPrefix() + ":" + key;
        return getRedisTemplate().hasKey(realKey);
    }

    /**
     * 增加(自增长), 负数则为自减
     */
    public static Long incr(String key, long increment) {
        return getRedisTemplate().opsForValue().increment(key, increment);
    }

    /**
     * 增加(自增长), 负数则为自减
     */
    public static Long incr(AbstractRedisKey prefix, String key, long increment) {
        String realKey = prefix.getPrefix() + ":" + key;
        return getRedisTemplate().opsForValue().increment(realKey, increment);
    }
}
