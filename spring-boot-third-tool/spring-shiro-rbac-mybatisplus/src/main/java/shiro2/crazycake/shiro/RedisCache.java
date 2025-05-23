package shiro2.crazycake.shiro;

import shiro2.crazycake.shiro.exception.PrincipalIdNullException;
import shiro2.crazycake.shiro.exception.PrincipalInstanceException;
import shiro2.crazycake.shiro.exception.SerializationException;
import shiro2.crazycake.shiro.serializer.ObjectSerializer;
import shiro2.crazycake.shiro.serializer.RedisSerializer;
import shiro2.crazycake.shiro.serializer.StringSerializer;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RedisCache<K, V> implements Cache<K, V> {

    private static final Logger logger = LoggerFactory.getLogger(RedisCache.class);

    private RedisSerializer keySerializer = new StringSerializer();
    private RedisSerializer valueSerializer = new ObjectSerializer();
    private IRedisManager redisManager;
    private String keyPrefix = "";
    private int expire = 0;
    private String principalIdFieldName = RedisCacheManager.DEFAULT_PRINCIPAL_ID_FIELD_NAME;

    /**
     * Construction
     *
     * @param redisManager
     */
    public RedisCache(IRedisManager redisManager, RedisSerializer keySerializer, RedisSerializer valueSerializer, String prefix, int expire, String principalIdFieldName) {
        if (redisManager == null) {
            throw new IllegalArgumentException("redisManager cannot be null.");
        }
        this.redisManager = redisManager;
        if (keySerializer == null) {
            throw new IllegalArgumentException("keySerializer cannot be null.");
        }
        this.keySerializer = keySerializer;
        if (valueSerializer == null) {
            throw new IllegalArgumentException("valueSerializer cannot be null.");
        }
        this.valueSerializer = valueSerializer;
        if (prefix != null && !"".equals(prefix)) {
            this.keyPrefix = prefix;
        }
        if (expire != -1) {
            this.expire = expire;
        }
        if (principalIdFieldName != null && !"".equals(principalIdFieldName)) {
            this.principalIdFieldName = principalIdFieldName;
        }
    }

    @Override
    public V get(K key) throws CacheException {
        logger.debug("get key [{}]", key);

        if (key == null) {
            return null;
        }

        try {
            Object redisCacheKey = getRedisCacheKey(key);
            byte[] rawValue = redisManager.get(keySerializer.serialize(redisCacheKey));
            if (rawValue == null) {
                return null;
            }
            return (V) valueSerializer.deserialize(rawValue);
        } catch (SerializationException e) {
            throw new CacheException(e);
        }
    }

    @Override
    public V put(K key, V value) throws CacheException {
        logger.debug("put key [{}]", key);
        if (key == null) {
            logger.warn("Saving a null key is meaningless, return value directly without call Redis.");
            return value;
        }
        try {
            Object redisCacheKey = getRedisCacheKey(key);
            redisManager.set(keySerializer.serialize(redisCacheKey), value != null ? valueSerializer.serialize(value) : null, expire);
            return value;
        } catch (SerializationException e) {
            throw new CacheException(e);
        }
    }

    @Override
    public V remove(K key) throws CacheException {
        logger.debug("remove key [{}]", key);
        if (key == null) {
            return null;
        }
        try {
            Object redisCacheKey = getRedisCacheKey(key);
            byte[] rawValue = redisManager.get(keySerializer.serialize(redisCacheKey));
            V previous = (V) valueSerializer.deserialize(rawValue);
            redisManager.del(keySerializer.serialize(redisCacheKey));
            return previous;
        } catch (SerializationException e) {
            throw new CacheException(e);
        }
    }

    private Object getRedisCacheKey(K key) {
        if (key == null) {
            return null;
        }
        if (keySerializer instanceof StringSerializer) {
            return this.keyPrefix + getStringRedisKey(key);
        }
        return key;
    }

    private String getStringRedisKey(K key) {
        String redisKey;
        if (key instanceof PrincipalCollection) {
            redisKey = getRedisKeyFromPrincipalIdField((PrincipalCollection) key);
        } else {
            redisKey = key.toString();
        }
        return redisKey;
    }

    private String getRedisKeyFromPrincipalIdField(PrincipalCollection key) {
        String redisKey;
        Object principalObject = key.getPrimaryPrincipal();
        Method pincipalIdGetter = null;
        Method[] methods = principalObject.getClass().getDeclaredMethods();
        for (Method m : methods) {
            if (RedisCacheManager.DEFAULT_PRINCIPAL_ID_FIELD_NAME.equals(this.principalIdFieldName)
                    && ("getAuthCacheKey".equals(m.getName()) || "getId".equals(m.getName()))) {
                pincipalIdGetter = m;
                break;
            }
            if (m.getName().equals("get" + this.principalIdFieldName.substring(0, 1).toUpperCase() + this.principalIdFieldName.substring(1))) {
                pincipalIdGetter = m;
                break;
            }
        }
        if (pincipalIdGetter == null) {
            throw new PrincipalInstanceException(principalObject.getClass(), this.principalIdFieldName);
        }

        try {
            Object idObj = pincipalIdGetter.invoke(principalObject);
            if (idObj == null) {
                throw new PrincipalIdNullException(principalObject.getClass(), this.principalIdFieldName);
            }
            redisKey = idObj.toString();
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new PrincipalInstanceException(principalObject.getClass(), this.principalIdFieldName, e);
        }

        return redisKey;
    }


    @Override
    public void clear() throws CacheException {
        logger.debug("clear cache");
        Set<byte[]> keys = null;
        try {
            keys = redisManager.keys(keySerializer.serialize(this.keyPrefix + "*"));
        } catch (SerializationException e) {
            logger.error("get keys error", e);
        }
        if (keys == null || keys.size() == 0) {
            return;
        }
        for (byte[] key : keys) {
            redisManager.del(key);
        }
    }

    @Override
    public int size() {
        Long longSize = 0L;
        try {
            longSize = new Long(redisManager.dbSize(keySerializer.serialize(this.keyPrefix + "*")));
        } catch (SerializationException e) {
            logger.error("get keys error", e);
        }
        return longSize.intValue();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<K> keys() {
        Set<byte[]> keys = null;
        try {
            keys = redisManager.keys(keySerializer.serialize(this.keyPrefix + "*"));
        } catch (SerializationException e) {
            logger.error("get keys error", e);
            return Collections.emptySet();
        }

        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptySet();
        }

        Set<K> convertedKeys = new HashSet<K>();
        for (byte[] key : keys) {
            try {
                convertedKeys.add((K) keySerializer.deserialize(key));
            } catch (SerializationException e) {
                logger.error("deserialize keys error", e);
            }
        }
        return convertedKeys;
    }

    @Override
    public Collection<V> values() {
        Set<byte[]> keys = null;
        try {
            keys = redisManager.keys(keySerializer.serialize(this.keyPrefix + "*"));
        } catch (SerializationException e) {
            logger.error("get values error", e);
            return Collections.emptySet();
        }

        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptySet();
        }

        List<V> values = new ArrayList<V>(keys.size());
        for (byte[] key : keys) {
            V value = null;
            try {
                value = (V) valueSerializer.deserialize(redisManager.get(key));
            } catch (SerializationException e) {
                logger.error("deserialize values= error", e);
            }
            if (value != null) {
                values.add(value);
            }
        }
        return Collections.unmodifiableList(values);
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public String getPrincipalIdFieldName() {
        return principalIdFieldName;
    }

    public void setPrincipalIdFieldName(String principalIdFieldName) {
        this.principalIdFieldName = principalIdFieldName;
    }
}
