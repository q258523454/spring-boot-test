package com.ratelimitbase.exception;

import lombok.Getter;

/**
 * RateLimiterErrorCode
 *
 * @author zz/z0zz
 * @since 2023-06-14 11:42
 */
@Getter
public enum RateLimiterErrorCode {

    /**
     * 命中限流
     */
    HIT_RATE_LIMIT_ERROR(100001, "key:{0},hit rateLimiter rule"),

    /**
     * 无callback方法
     */
    CALLBACK_METHOD_NOT_EXIST(100002, "key:{0},callBack method not exist"),

    /**
     * 禁止重复注册限流方法
     */
    FORBIDDEN_REPEAT_REGISTER(100003, "forbidden repeat register ratelimit, already exists:{0}"),

    /**
     * 限流方法未注册
     */
    RATE_LIMIT_TYPE_NOT_REGISTER(100004, "rate limit type:{0} not register"),

    /**
     * 限流方法未注册
     */
    RATE_LIMIT_MUST_POSITIVE(100005, "rate limit must be greater than 0"),

    /**
     * 限流方法未注册
     */
    RATE_LIMIT_OUT_OF_RANGE(100006, "Redis bucket rate limit out of range"),
    ;

    private final Integer code;

    private final String message;

    RateLimiterErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
