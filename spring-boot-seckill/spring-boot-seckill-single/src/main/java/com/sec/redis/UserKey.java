package com.sec.redis;

import com.sec.redis.abstracts.AbstractRedisKey;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @date 2021-08-04 20:33
 * @modify
 */
@Slf4j
public class UserKey extends AbstractRedisKey {

    public UserKey(String prefix, int expireSeconds) {
        super(prefix, expireSeconds);
    }

    /**
     * User TOKEN 存储信息, 超时2天 (秒)
     */
    public static final UserKey TOKEN = new UserKey("user", 3600 * 24 * 2);

    /**
     * User ID 存储信息, 不过期
     */
    public static final UserKey USER = new UserKey("id", 0);

}
