package com.rtuse.controller;

import com.alibaba.fastjson.JSON;
import com.ratelimitbase.aspect.RateLimiterAnnotation;
import com.ratelimitbase.enums.RateLimiterTypeEnum;
import com.ratelimitbase.strategy.register.RedisCountRateLimiter;
import com.rtuse.pojo.Student;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestRedisBucketController {

    @Autowired(required = false)
    private RedisCountRateLimiter redisCountRateLimiter;

    /**
     * 限流limit 小于 限流时间
     */
    @PostMapping(value = "/ratelimit/redis/bucket")
    @RateLimiterAnnotation(type = RateLimiterTypeEnum.REDIS_BUCKET, limit = 3, callBackMethod = "redisLimitCallBack")
    public String test1(@RequestBody Student student, @RequestParam String id) {
        return "index";
    }


    /**
     * 限流limit 大于 限流时间
     */
    @PostMapping(value = "/ratelimit/redis/bucket2")
    @RateLimiterAnnotation(type = RateLimiterTypeEnum.REDIS_BUCKET, limit = 2001, callBackMethod = "redisLimitCallBack")
    public String test2(@RequestBody Student student, @RequestParam String id) {
        return "index";
    }

    /**
     * 限流回调方法
     */
    public void redisLimitCallBack(Student student, String id) {
        log.info("id:{}", id);
        log.info("student:{}", JSON.toJSONString(student));
        log.error("callBack1 execute");
    }
}
