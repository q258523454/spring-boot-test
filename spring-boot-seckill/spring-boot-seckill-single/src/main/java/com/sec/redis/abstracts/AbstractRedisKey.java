package com.sec.redis.abstracts;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @date 2021-08-04 20:16
 * @modify
 */
@Slf4j
public class AbstractRedisKey implements RedisKeyInterface {

    /**
     * 前缀
     */
    private String prefix;

    /**
     * 有效期， 0:永久
     */
    private int expireSeconds;

    public AbstractRedisKey(String prefix, int expireSeconds) {
        this.prefix = prefix;
        this.expireSeconds = expireSeconds;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public int getExpireSeconds() {
        return expireSeconds;
    }
}
