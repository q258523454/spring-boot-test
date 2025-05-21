package com.ratelimitbase.strategy.factory;

import com.ratelimitbase.enums.RateLimiterTypeEnum;
import com.ratelimitbase.exception.RateLimiterErrorCode;
import com.ratelimitbase.exception.RateLimiterException;
import com.ratelimitbase.strategy.AbstractRateLimiterStrategy;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.ObjectUtils;

import java.text.MessageFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 限流策略工厂类
 *
 * @author zz/z0zz
 * @since 2023-06-14 11:42
 */
@Slf4j
public class RateLimiterStrategyFactory {
    /***
     * 限流工厂map
     */
    private static final ConcurrentMap<RateLimiterTypeEnum, AbstractRateLimiterStrategy> RATE_LIMIT_MAP = new ConcurrentHashMap<>();

    /**
     * 注册限流策略
     */
    public static void register(RateLimiterTypeEnum rateLimiterTypeEnum, AbstractRateLimiterStrategy strategy) {
        if (ObjectUtils.isEmpty(rateLimiterTypeEnum)) {
            throw new IllegalArgumentException("RateLimiterType can not be null");
        }
        if (ObjectUtils.isEmpty(strategy)) {
            throw new IllegalArgumentException("AbstractRateLimiterStrategy can not be null");
        }
        AbstractRateLimiterStrategy olderStrategy = RATE_LIMIT_MAP.put(rateLimiterTypeEnum, strategy);
        if (olderStrategy != null) {
            // 禁止重复注册限流方法
            Integer errorCode = RateLimiterErrorCode.FORBIDDEN_REPEAT_REGISTER.getCode();
            String errorMsg = MessageFormat.format(RateLimiterErrorCode.FORBIDDEN_REPEAT_REGISTER.getMessage(), rateLimiterTypeEnum.getCode());
            throw new RateLimiterException(errorCode, errorMsg);

        }
        log.info("Register rateLimiter type:[{}], class:[{}]", rateLimiterTypeEnum.name(), strategy.getClass().getCanonicalName());
    }

    /**
     * 获取限流策略实例
     */
    public static AbstractRateLimiterStrategy getRateLimiterByEnum(RateLimiterTypeEnum rateLimiterTypeEnum) {
        if (ObjectUtils.isEmpty(rateLimiterTypeEnum)) {
            throw new IllegalArgumentException("rateLimiterType can not be null");
        }
        return RATE_LIMIT_MAP.get(rateLimiterTypeEnum);
    }
}
