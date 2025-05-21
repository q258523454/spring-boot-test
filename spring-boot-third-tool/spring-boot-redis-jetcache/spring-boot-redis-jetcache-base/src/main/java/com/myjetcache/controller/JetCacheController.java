package com.myjetcache.controller;

import com.alibaba.fastjson.JSON;
import com.myjetcache.entity.Student;
import com.myjetcache.service.JetCacheBaseService;

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

    /**
     * 增
     */
    @GetMapping("/jetcache/add")
    public String add() {
        Student student = Student.getStudent("1");
        Student add = jetCacheBaseService.add(student);
        return JSON.toJSONString(add);
    }

    /**
     * 删
     */
    @GetMapping("/jetcache/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        jetCacheBaseService.delete(id);
        log.info("delete done");
    }

    /**
     * 改
     */
    @GetMapping("/jetcache/update")
    public void update() {
        Student student = Student.getStudent("1");
        jetCacheBaseService.update(student);
        log.info("update done");
    }

    /**
     * 查
     */
    @GetMapping("/jetcache/get/{id}")
    public String get(@PathVariable("id") Long id) {
        Student student = jetCacheBaseService.get(id);
        return JSON.toJSONString(student);
    }

    /**
     * 条件查
     */
    @GetMapping("/jetcache/condition/get/{id}")
    public String get(@PathVariable("id") Long id, @RequestParam("isUseCache") boolean isUseCache) {
        Student student = jetCacheBaseService.get(id, isUseCache);
        return JSON.toJSONString(student);
    }
}
