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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

@Slf4j
@Service
@ConditionalOnBean(RedisTemplate.class)
public class RedisBucketRateLimiter extends AbstractRateLimiterStrategy implements InitializingBean {

    public static final long SECOND_IN_MILLS = 1000L;

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
        getRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("scripts/rateLimter_bucket.lua")));
        log.info("RedisBucketRateLimiter [分布式限流处理器]脚本加载完成");
    }

    /**
     * 令牌桶原理:
     * 令牌桶是漏桶算法的改进版，漏桶是匀速处理请求，无法应对瞬时并发。
     * 而令牌桶是创建一个固定大小的桶(这里大小就用limit), 然后均匀的速度往里面放令牌，能应对瞬时并发。
     * 注意:
     * 1.可用令牌永远不会超过桶大小
     * 2.这里的放令牌时间间隔最小单位为ms毫秒, 即1秒的时间最多放1000块令牌
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object handle(ProceedingJoinPoint pjp, RateLimiterAnnotation rateLimiterAnnotationMethod) throws Throwable {
        long limit = rateLimiterAnnotationMethod.limit();
        // TODO: 改成 redis 获取
        long nowTime = System.currentTimeMillis();
        // 每次向桶中放多少个令牌
        double stepValue;
        // 向桶中放令牌的每次间隔时间
        long stepTime;
        // 总共放多少次
        long stepNum;
        // 限流时间,默认1000毫秒(1秒), 放令牌的间隔最快只能是1毫秒
        long limitTimeInMills = getLimitTimeInMills();
        // 限流次数大于限流毫秒数
        if (limit > limitTimeInMills) {
            // 放令牌间隔 1 毫秒
            stepTime = 1;
            // 总共放次数等于毫秒数(limitTimeInMills)
            stepNum = limitTimeInMills;
            // 每次放 limit/stepNum 个
            stepValue = new BigDecimal(limit).divide(new BigDecimal(stepNum), 3, RoundingMode.FLOOR).doubleValue();
        } else {
            // 每次向桶中放1个令牌
            stepValue = 1L;
            // 总共放limit次
            stepNum = limit;
            // 向桶中放令牌的每次间隔时间
            stepTime = (long) Math.floor(limitTimeInMills * 1.0 / stepNum);
        }
        String limiterKey = RateLimiterKeyUtils.createRateLimiterKey(pjp, rateLimiterAnnotationMethod);
        List<String> keyList = new ArrayList<>();
        keyList.add(limiterKey);
        Long result = (Long) redisTemplate.execute(getRedisScript, keyList, limit, stepValue, stepTime, nowTime);
        if (ObjectUtils.isEmpty(result) || result == -1) {
            return callBackMethodExecute(limiterKey, pjp, rateLimiterAnnotationMethod);
        }
        log.info("redis bucket limit exec, result:{}", result);
        return pjp.proceed();
    }

    /**
     * 限流时间窗口,默认1000毫秒(1秒),超过这个窗口没有API调用,则会清除key缓存
     */
    private long getLimitTimeInMills() {
        long limitTime = 1000L;
        String stepValueStr = environmentConfig.getPropertyValue("ratelimit.redis.bucket.limitTime", false);
        if (!StringUtils.isEmpty(stepValueStr)) {
            limitTime = Long.parseLong(stepValueStr);
            if (limitTime <= 0) {
                throw new IllegalArgumentException("redis bucket limit time must be positive");
            }
        }
        return limitTime;
    }


    @Override
    public void afterPropertiesSet() {
        RateLimiterStrategyFactory.register(RateLimiterTypeEnum.REDIS_BUCKET, this);
    }
}
