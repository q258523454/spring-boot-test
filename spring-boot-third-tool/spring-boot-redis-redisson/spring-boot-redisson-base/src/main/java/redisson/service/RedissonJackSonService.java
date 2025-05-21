

package redisson.service;

import redisson.service.base.AbstractRedissonService;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component
public class RedissonJackSonService extends AbstractRedissonService {

    @Autowired
    @Qualifier("redissonClient")
    private RedissonClient redissonClient;

    @Override
    public RedissonClient getRedissonClient() {
        return redissonClient;
    }
}
