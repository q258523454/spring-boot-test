package com.ratelimitbase.strategy.register;

import com.google.common.util.concurrent.RateLimiter;
import com.ratelimitbase.aspect.RateLimiterAnnotation;
import com.ratelimitbase.enums.RateLimiterTypeEnum;
import com.ratelimitbase.strategy.AbstractRateLimiterStrategy;
import com.ratelimitbase.strategy.factory.RateLimiterStrategyFactory;
import com.ratelimitbase.utils.JoinPointUtils;
import com.ratelimitbase.utils.RateLimiterKeyUtils;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Guava RateLimiter限流实现
 *
 * @author zz/z0zz
 * @since 2023-06-14 11:42
 */
@Service
@Slf4j
@SuppressWarnings("UnstableApiUsage")
public class GuavaRateLimiter extends AbstractRateLimiterStrategy implements InitializingBean {

    private static final ConcurrentHashMap<String, RateLimiter> RATE_LIMIT_GUAVA_MAP = new ConcurrentHashMap<>();

    /**
     * 微秒
     */
    private static final long MICROSECONDS_OF_ONE_SECOND = 1000 * 1000L;

    @Override
    public Object handle(ProceedingJoinPoint pjp, RateLimiterAnnotation rateLimiterAnnotationMethod) throws Throwable {
        String rateLimiterKey = RateLimiterKeyUtils.createRateLimiterKey(pjp, rateLimiterAnnotationMethod);
        long limit = rateLimiterAnnotationMethod.limit();
        RateLimiter rateLimiter = getGuavaRateLimiter(rateLimiterKey, rateLimiterAnnotationMethod);
        if (rateLimiter == null) {
            log.error("Guava RateLimiter is null, method:[{}]", pjp.getSignature().toLongString());
            return pjp.proceed();
        }
        // log.info("Guava rate limit, key:[{}],limit:[{}]", rateLimiterKey, limit);
        // limit微秒间隔
        long timeout = MICROSECONDS_OF_ONE_SECOND / limit;
        // 限流后,进入限流处理逻辑
        if (!rateLimiter.tryAcquire(timeout, TimeUnit.MICROSECONDS)) {
            return callBackMethodExecute(rateLimiterKey, pjp, rateLimiterAnnotationMethod);
        }
        return pjp.proceed();
    }

    /**
     * 获取 Guava RateLimiter
     */
    private RateLimiter getGuavaRateLimiter(String key, RateLimiterAnnotation rateLimiterAnnotationMethod) {
        RateLimiter result = RATE_LIMIT_GUAVA_MAP.get(key);
        if (result == null) {
            log.info("RateLimiter map not contain rate limit key:[{}], ready to create key in map.", key);
            RateLimiter rateLimiter = RateLimiter.create(rateLimiterAnnotationMethod.limit());
            result = rateLimiter;
            RateLimiter putByOtherThread = RATE_LIMIT_GUAVA_MAP.putIfAbsent(key, rateLimiter);
            // 防止多线程环境下相同key被覆盖
            if (putByOtherThread != null) {
                result = putByOtherThread;
            }
        }
        return result;
    }

    @Override
    public void afterPropertiesSet() {
        RateLimiterStrategyFactory.register(RateLimiterTypeEnum.GUAVA, this);
    }
}
