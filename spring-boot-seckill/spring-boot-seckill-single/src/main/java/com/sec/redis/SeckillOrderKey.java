package com.sec.redis;

import com.sec.redis.abstracts.AbstractRedisKey;

public class SeckillOrderKey extends AbstractRedisKey {
    public SeckillOrderKey(String prefix, int expireSeconds) {
        super(prefix, expireSeconds);
    }

    public static final SeckillOrderKey USER_ORDER = new SeckillOrderKey("seckill_order", 3600);
}
