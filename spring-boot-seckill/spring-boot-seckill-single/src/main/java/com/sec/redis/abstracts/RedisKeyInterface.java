package com.sec.redis.abstracts;

/**
 * @Description
 * @date 2021-08-04 20:24
 * @modify
 */
public interface RedisKeyInterface {

    /**
     * key 前缀
     * @return key 前缀
     */
    String getPrefix();


    /**
     * 有效期
     * @return 有效期
     */
    int getExpireSeconds();

}
