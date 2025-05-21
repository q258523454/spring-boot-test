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

import java.util.HashMap;
import java.util.Map;


@RestController
public class ControllerMapTest {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StudentService studentService;


    /**
     * Map 等结构不能直接用 protoStuff 序列化, 需要借助包装类
     */
    @PostMapping(value = "/protoStuff/set/map")
    public String insertMap(@RequestParam("key") String key, @RequestBody Student student) {
        Map<String, Student> map = new HashMap<>();
        map.put("map", student);
        return RedisCacheUtil.putCacheProWithExpireTime(key, map, 60) + "";
    }

    @GetMapping(value = "/protoStuff/get/map")
    public String selectMap(@RequestParam(value = "key") String key) {
        Map<String, Student> cachePro = RedisCacheUtil.getCachePro(key, Map.class);
        return JSONObject.toJSONString(cachePro);
    }
}
