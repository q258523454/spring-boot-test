package com.redis.controller;

import com.alibaba.fastjson.JSONObject;
import com.redis.entity.Student;
import com.redis.redisUtil.RedisCacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by
 *
 * @author :   zhangjian
 * @date :   2018-08-27
 */

@RestController
public class Controller_Redis {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedisCacheUtil redisCacheUtil;

    @PostMapping(value = "/redis_insertStudent")
    public String insertStundet(@RequestParam("key") String key, @RequestParam("value") String value) {
        redisTemplate.opsForValue().set(key, value);
        return "finish redisUtil task";
    }

    @GetMapping(value = "/redis_selectStudentByName")
    public String selectStudent(@RequestParam(value = "key") String key) {
        String value = redisTemplate.opsForValue().get(key);
        return "finish redisUtil task, name is :" + value;
    }

    // redis插入对象，prostuff序列化
    @PostMapping(value = "/redis_insertStudentObject")
    public String insertStudentObject(@RequestParam("key") String key, Student student) {
        redisCacheUtil.putCachePro(key, student);
        return "finish redisUtil task";
    }

    @GetMapping(value = "/redis_selectStudentObjectByKey")
    public String selectStudentObjectByKey(@RequestParam(value = "key") String key) {
        Student value = redisCacheUtil.getCachePro(key, Student.class);
        return "finish redisUtil task, Student is :" + JSONObject.toJSONString(value);
    }

}
