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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
public class ControllerListTest {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final String CACHE_KEY = RedisCacheUtil.CAHCE_NAME + "|getUserListPro";


    @Autowired
    private StudentService studentService;

    /**
     * List 存
     */
    @PostMapping(value = "/protostuff/student/list/set", produces = "application/json; charset=UTF-8")
    public String addlist(HttpServletRequest request, HttpServletResponse response) {
        List<Student> studentList = studentService.selectAll();
        boolean res = RedisCacheUtil.putListCacheProWithExpireTime(CACHE_KEY, studentList, RedisCacheUtil.CAHCE_TIME);
        return res + "";
    }

    /**
     * List 查
     */
    @GetMapping(value = "/protostuff/student/list/get", produces = "application/json; charset=UTF-8")
    public String getlist(HttpServletRequest request, HttpServletResponse response) {
        List<Student> studentList = RedisCacheUtil.getListCachePro(CACHE_KEY, Student.class);
        return JSONObject.toJSONString(studentList);
    }
}
