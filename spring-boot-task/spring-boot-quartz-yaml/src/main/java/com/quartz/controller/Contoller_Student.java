package com.quartz.controller;


import com.alibaba.fastjson.JSON;
import com.quartz.service.StudentSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Date: 2019-05-10
 * @Version 1.0
 */

@RestController
public class Contoller_Student {

    @Autowired
    private StudentSerivce studentSerivce;

    @GetMapping(value = "/selectAll", produces = "application/json; charset=UTF-8")
    public @ResponseBody
    String selectAll() {
        return JSON.toJSONString(studentSerivce.selectAll());
    }
}
