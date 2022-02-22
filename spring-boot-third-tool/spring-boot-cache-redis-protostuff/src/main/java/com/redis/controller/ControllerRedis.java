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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by
 *
 * @date :   2018-08-27
 */

@RestController
public class ControllerRedis {

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
     *
     * @param key
     * @param value
     * @return
     */
    @PostMapping(value = "/set")
    public String insertStudentObject(@RequestParam("key") String key, String value) {
        RedisCacheUtil.set(key, value);
        return "finish redisUtil set task";
    }

    /**
     * 普通查询
     *
     * @param key
     * @return
     */
    @PostMapping(value = "/get")
    public String insertStudentObject(@RequestParam("key") String key) {
        return RedisCacheUtil.get(key);
    }

    /**
     * prostuff序列化——增
     */
    @PostMapping(value = "/redis_insertStudentObject")
    public String insertStudentObject(@RequestParam("key") String key, Student student) {
        RedisCacheUtil.putCachePro(key, student);
        return "finish redisUtil task";
    }

    /**
     * prostuff序列化——查
     */
    @GetMapping(value = "/redis_selectStudentObjectByKey")
    public String selectStudentObjectByKey(@RequestParam(value = "key") String key) {
        Student value = RedisCacheUtil.getCachePro(key, Student.class);
        return "finish redisUtil task,key:{" + key + "} student is :" + JSONObject.toJSONString(value);
    }

    /**
     * Redis-作为缓存—查
     */
    @GetMapping(value = "/selectAllUserPro", produces = "application/json; charset=UTF-8")
    public String selectAllUserPro(HttpServletRequest request, HttpServletResponse response) {
        List<Student> myUserList = studentService.selectAllUserPro();
        return JSONObject.toJSONString(myUserList);
    }

}
