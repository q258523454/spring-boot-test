package com.redis.controller;

import com.alibaba.fastjson.JSONObject;
import com.redis.entity.Student;
import com.redis.entity.StudentListObject;
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


@RestController
public class ControllerObjectTest {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StudentService studentService;


    /**
     * protoStuff 序列化——增
     */
    @PostMapping(value = "/protostuff/list/object/set")
    public String insertStudentObject(@RequestParam("key") String key, @RequestBody Student student) {
        StudentListObject studentListObject = new StudentListObject();
        studentListObject.setName("复杂类型Object");
        studentListObject.getStudentList().add(student);
        studentListObject.getStudentList().add(student);
        return RedisCacheUtil.putCacheProWithExpireTime(key, studentListObject, 60) + "";
    }

    /**
     * prostuff序列化——查
     */
    @GetMapping(value = "/protostuff/list/object/get")
    public String selectStudentObjectByKey(@RequestParam(value = "key") String key) {
        StudentListObject value = RedisCacheUtil.getCachePro(key, StudentListObject.class);
        return JSONObject.toJSONString(value);
    }
}
