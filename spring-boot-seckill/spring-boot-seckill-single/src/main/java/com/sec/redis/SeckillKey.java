package com.sec.redis;


import com.sec.redis.abstracts.AbstractRedisKey;

public class SeckillKey extends AbstractRedisKey {
    public SeckillKey(String prefix, int expireSeconds) {
        super(prefix, expireSeconds);
    }

    public static final SeckillKey GOODS_OVER = new SeckillKey("over", 0);


}
