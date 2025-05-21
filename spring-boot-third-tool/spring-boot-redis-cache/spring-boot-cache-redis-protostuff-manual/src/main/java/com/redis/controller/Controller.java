package com.redis.controller;

import com.alibaba.fastjson.JSONObject;
import com.redis.entity.Student;
import com.redis.redisUtil.RedisCacheUtil;
import com.redis.service.StudentService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by
 *
 * @date :   2018-08-27
 */

@RestController
public class Controller {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StudentService studentService;

    /*
    // 默认序列化插入
    @PostMapping(value = "/redis_insertStudent")
    public String insertStundet(@RequestParam("key") String key, @RequestParam("value") String value) {
        redisTemplate.opsForValue().set(key, value);
        return "finish redisUtil task";
    }

    // 默认序列化查询
    @GetMapping(value = "/redis_selectStudentByName")
    public String selectStudent(@RequestParam(value = "key") String key) {
        String value = redisTemplate.opsForValue().get(key);
        return "finish redisUtil task, name is :" + value;
    }
    */

    /**
     * 普通新增
     */
    @PostMapping(value = "/base/set")
    public String insertStudentObject(@RequestParam("key") String key, String value) {
        RedisCacheUtil.set(key, value);
        return "finish redisUtil set task";
    }

    /**
     * 普通查询
     */
    @PostMapping(value = "/base/get")
    public String insertStudentObject(@RequestParam("key") String key) {
        return RedisCacheUtil.get(key);
    }

    /**
     * protoStuff 序列化——增
     * 当对象只包含基本数据类型的时候,RADM工具可以直接展示数据. 非基本数据类型的字段, 默认不会展示.
     */
    @PostMapping(value = "/protostuff/student/set")
    public String insertStudentObject(@RequestParam("key") String key, @RequestBody Student student) {
        // 当对象只包含基本数据类型的时候,RADM工具可以直接展示数据. 非基本数据类型的字段, 默认不会展示.
        return RedisCacheUtil.putCacheProWithExpireTime(key, student, 60) + "";
    }

    /**
     * protoStuff 序列化——查
     */
    @GetMapping(value = "/protostuff/student/get")
    public String selectStudentObjectByKey(@RequestParam(value = "key") String key) {
        Student value = RedisCacheUtil.getCachePro(key, Student.class);
        return JSONObject.toJSONString(value);
    }
}
