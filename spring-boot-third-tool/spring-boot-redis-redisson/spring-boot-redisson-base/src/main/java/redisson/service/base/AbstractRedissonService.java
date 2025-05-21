

package redisson.service.base;

import org.redisson.api.BatchResult;
import org.redisson.api.RBatch;
import org.redisson.api.RLock;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public abstract class AbstractRedissonService implements RedissonClientInterface {

    /**
     * 默认保存时间
     */
    private static final long DEFAULT_EXPIRE_TIME_SECONDS = 3600L;


    public RLock getLock(String key) {
        return getRedissonClient().getLock(key);
    }

    /**
     * 设置key-value
     */
    public void set(String key, Object value, long seconds) {
        if (Objects.isNull(value)) {
            return;
        }
        if (seconds <= 0) {
            seconds = DEFAULT_EXPIRE_TIME_SECONDS;
        }
        getRedissonClient().getBucket(key).set(value, seconds, TimeUnit.SECONDS);
    }

    /**
     * 批量设置key-value
     */
    public <T> void setBatch(Map<String, T> map, long seconds) {
        if (seconds <= 0) {
            seconds = DEFAULT_EXPIRE_TIME_SECONDS;
        }
        RBatch batch = getRedissonClient().createBatch();
        long finalSeconds = seconds;
        map.forEach((k, v) -> {
            batch.getBucket(k).setAsync(v, finalSeconds, TimeUnit.SECONDS);
        });
        batch.execute();
    }


    /**
     * 获取value
     * 没有则返回 null
     */
    public Object get(String key) {
        return getRedissonClient().getBucket(key).get();
    }

    /**
     * 批量获取value
     */
    public List<?> getBatch(List<String> keys) {
        RBatch batch = getRedissonClient().createBatch();
        keys.forEach(key -> batch.getBucket(key).getAsync());
        BatchResult<?> execute = batch.execute();
        return execute.getResponses();
    }


    /**
     * 设置key-value,同时返回旧值
     * 旧值不存在则返回 null
     */
    public Object getAndSet(String key, Object value, long seconds) {
        if (seconds <= 0) {
            seconds = DEFAULT_EXPIRE_TIME_SECONDS;
        }
        return getRedissonClient().getBucket(key).getAndSet(value, seconds, TimeUnit.SECONDS);
    }

    /**
     * 获取所有的指定前缀 keys
     * 例如: "test:*"
     */
    public Set<String> getKeys(String prefix) {
        Iterable<String> keysByPattern = getRedissonClient().getKeys().getKeysByPattern(prefix);
        Set<String> keys = new HashSet<>();
        for (String key : keysByPattern) {
            keys.add(key);
        }
        return keys;
    }

    /**
     * 删除key
     */
    public boolean removeKey(String key) {
        return getRedissonClient().getBucket(key).delete();
    }

    /**
     * 删除key
     */
    public void removeKeyAsync(String key) {
        getRedissonClient().getBucket(key).deleteAsync();

    }

    /**
     * 批量删除
     */
    public void removeKeyBatch(Collection<String> keys) {
        RBatch batch = getRedissonClient().createBatch();
        keys.forEach(key -> batch.getBucket(key).deleteAsync());
        batch.execute();
    }
}
