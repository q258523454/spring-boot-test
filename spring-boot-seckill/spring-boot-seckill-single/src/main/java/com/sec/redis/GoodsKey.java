package com.sec.redis;


import com.sec.redis.abstracts.AbstractRedisKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GoodsKey extends AbstractRedisKey {

    public GoodsKey(String prefix, int expireSeconds) {
        super(prefix, expireSeconds);
    }

    public static final GoodsKey GOOD_STOCK = new GoodsKey("stock", 0);
}
