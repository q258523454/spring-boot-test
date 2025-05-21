package com.ratelimitbase.enums;

/**
 * 限流算法的类型
 *
 * @author zz/z0zz
 * @since 2023-06-14 11:42
 */
public enum RateLimiterTypeEnum {

    /**
     * Guava RateLimiter
     */
    GUAVA("guava", "Guava RateLimiter算法"),

    /**
     * Redis RateLimiter
     */
    REDIS_COUNT("redis_count", "Redis 计数器算法"),

    /**
     * Redis Bucket
     */
    REDIS_BUCKET("redis_bucket", "Redis 令牌桶算法"),
    ;

    private final String code;
    private final String desc;

    RateLimiterTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static RateLimiterTypeEnum parse(String code) {
        for (RateLimiterTypeEnum item : values()) {
            if (item.code.equals(code)) {
                return item;
            }
        }
        throw new IllegalArgumentException(code + " not exists");
    }
}
