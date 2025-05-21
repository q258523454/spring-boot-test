package com.redis.redisUtil;

import com.redis.util.ProtoStuffUtil;
import com.redis.util.SpringContextHolder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Set;

/**
 * redis缓存
 */
public class RedisCacheUtil extends RedisBaseUtil {
    private static final Logger logger = LoggerFactory.getLogger(RedisBaseUtil.class);
    public static final String CAHCE_NAME = "my_cache"; // 缓存名
    public static final int CAHCE_TIME = 60;       // 默认缓存时间(秒)


    public static StringRedisTemplate getRedisTemplate() {
        return SpringContextHolder.getBean("stringRedisTemplate");
    }

    /**
     * 存入redis，不带过期时间
     */
    public static <T> boolean putCachePro(String key, T obj) {
        final byte[] bkey = key.getBytes();
        final byte[] bvalue = ProtoStuffUtil.serialize(obj);
        boolean result = getRedisTemplate().execute(new RedisCallback<Boolean>() {
            @Nullable
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.setNX(bkey, bvalue);
            }
        });
        return result;
    }


    /**
     * 存入redis，带过期时间
     */
    public static <T> boolean putCacheProWithExpireTime(String key, T obj, final long expireTime) {
        final byte[] bkey = key.getBytes();
        final byte[] bvalue = ProtoStuffUtil.serialize(obj);
        boolean result =getRedisTemplate().execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.setEx(bkey, expireTime, bvalue);
            }
        });
        return result;
    }

    public static <T> T getCachePro(final String key, Class<T> targetClass) {
        byte[] result = getRedisTemplate().execute(new RedisCallback<byte[]>() {
            @Override
            public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.get(key.getBytes());
            }
        });
        if (result == null) {
            return null;
        }
        return ProtoStuffUtil.deserialize(result, targetClass);
    }

    public static <T> boolean putListCachePro(String key, List<T> objList) {
        final byte[] bkey = key.getBytes();
        final byte[] bvalue = ProtoStuffUtil.serializeList(objList);
        boolean result = getRedisTemplate().execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.setNX(bkey, bvalue);
            }
        });
        return result;
    }

    public static <T> boolean putListCacheProWithExpireTime(String key, List<T> objList, final long expireTime) {
        final byte[] bKey = key.getBytes();
        final byte[] bValue = ProtoStuffUtil.serializeList(objList);
        boolean result = getRedisTemplate().execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                connection.setEx(bKey, expireTime, bValue);
                return true;
            }
        });
        return result;
    }


    public static <T> List<T> getListCachePro(final String key, Class<T> targetClass) {
        byte[] result = getRedisTemplate().execute(new RedisCallback<byte[]>() {
            @Override
            public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.get(key.getBytes());
            }
        });
        if (result == null) {
            return null;
        }
        return ProtoStuffUtil.deserializeList(result, targetClass);
    }

    /**
     * 精确删除key
     *
     * @param key
     */
    public static void deleteCache(String key) {
        getRedisTemplate().delete(key);
    }

    /**
     * 模糊删除key
     *
     * @param pattern
     */
    public static void deleteCacheWithPattern(String pattern) {
        Set<String> keys = getRedisTemplate().keys(pattern);
        getRedisTemplate().delete(keys);
    }

    /**
     * 清空所有缓存
     */
    public static void clearCache() {
        deleteCacheWithPattern(RedisCacheUtil.CAHCE_NAME + "|*");
    }


}