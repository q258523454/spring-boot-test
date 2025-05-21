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
public class TestRedisCountController {

    @PostMapping(value = "/ratelimit/redis/count")
    @RateLimiterAnnotation(type = RateLimiterTypeEnum.REDIS_COUNT, limit = 5, callBackMethod = "redisLimitCallBack")
    public String test1(@RequestBody Student student, @RequestParam String id) {
        log.info("id:{}", id);
        log.info("student:{}", JSON.toJSONString(student));
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
