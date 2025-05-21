package com.myjetcachekryo.controller;

import com.alibaba.fastjson.JSON;
import com.myjetcachekryo.entity.Student;
import com.myjetcachekryo.service.JetCacheBaseService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@Slf4j
public class JetCacheController {

    private final Random random = new Random();

    @Autowired
    private JetCacheBaseService jetCacheBaseService;


    @GetMapping("/jetcache/serializable/add")
    public String add() {
        Student student = Student.getStudent("1");
        Student add = jetCacheBaseService.add(student);
        return JSON.toJSONString(add);
    }

    @GetMapping("/jetcache/serializable/condition/get/{id}")
    public String get(@PathVariable("id") Long id, @RequestParam("isUseCache") boolean isUseCache) {
        Student res = jetCacheBaseService.get(id, isUseCache);
        return JSON.toJSONString(res);
    }

    @GetMapping("/jetcache/serializable/refresh/get")
    public String getStringCache() {
        Student student = jetCacheBaseService.getRefresh(1L);
        return JSON.toJSONString(student);

    }
}
