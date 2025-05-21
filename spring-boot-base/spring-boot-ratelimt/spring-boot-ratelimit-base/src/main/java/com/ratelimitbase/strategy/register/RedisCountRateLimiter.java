package com.ratelimitbase.strategy.register;

import com.ratelimitbase.aspect.RateLimiterAnnotation;
import com.ratelimitbase.config.EnvironmentConfig;
import com.ratelimitbase.enums.RateLimiterTypeEnum;
import com.ratelimitbase.strategy.AbstractRateLimiterStrategy;
import com.ratelimitbase.strategy.factory.RateLimiterStrategyFactory;
import com.ratelimitbase.utils.RateLimiterKeyUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

@Slf4j
@Service
@ConditionalOnBean(RedisTemplate.class)
public class RedisCountRateLimiter extends AbstractRateLimiterStrategy implements InitializingBean {

    @SuppressWarnings({"rawtypes"})
    @Autowired
    private RedisTemplate redisTemplate;


    @Autowired
    private EnvironmentConfig environmentConfig;


    private DefaultRedisScript<Long> getRedisScript;

    @PostConstruct
    public void init() {
        getRedisScript = new DefaultRedisScript<>();
        getRedisScript.setResultType(Long.class);
        getRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("scripts/rateLimter_count.lua")));
        log.info("RedisCountRateLimiter [分布式限流处理器]脚本加载完成");
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object handle(ProceedingJoinPoint pjp, RateLimiterAnnotation rateLimiterAnnotationMethod) throws Throwable {
        String limiterKey = RateLimiterKeyUtils.createRateLimiterKey(pjp, rateLimiterAnnotationMethod);
        List<String> keyList = new ArrayList<>();
        keyList.add(limiterKey);
        // 默认过期时间为1秒
        long expireTime = getExpireTime();
        Long result = (Long) redisTemplate.execute(getRedisScript, keyList, expireTime, rateLimiterAnnotationMethod.limit());
        if (ObjectUtils.isEmpty(result) || result == -1) {
            return callBackMethodExecute(limiterKey, pjp, rateLimiterAnnotationMethod);
        }
        log.info("redis count limit exec, result:{}", result);
        return pjp.proceed();
    }

    /**
     * 并发时间,默认1秒(即默认QPS)
     * redis count 模式限流key过期时间, 默认1s, 例如注解中 limit =10, 那么表示限流 qps=10
     * 同理,如果注解中 limit=10, ratelimit.redis.count.expireTime = 2, 那么限流qps=5
     * 可根据需要配置成其他, 例如 60s 内限流多少次
     */
    private long getExpireTime() {
        long expireTime = 1L;
        String expireTimeStr = environmentConfig.getPropertyValue("ratelimit.redis.count.expireTime", false);
        if (!StringUtils.isEmpty(expireTimeStr)) {
            expireTime = Long.parseLong(expireTimeStr);
            if (expireTime <= 0) {
                throw new IllegalArgumentException("redis count rate limit expire time must be positive");
            }
        }
        return expireTime;
    }

    @Override
    public void afterPropertiesSet() {
        RateLimiterStrategyFactory.register(RateLimiterTypeEnum.REDIS_COUNT, this);
    }
}
