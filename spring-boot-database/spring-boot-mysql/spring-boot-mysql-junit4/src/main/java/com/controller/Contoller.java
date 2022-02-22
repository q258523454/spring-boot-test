package com.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.service.StudentSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Date: 2019-05-10
 * @Version 1.0
 */

@RestController
public class Contoller {

    @Autowired
    private StudentSerivce studentSerivce;

    @GetMapping(value = "/selectAll", produces = "application/json; charset=UTF-8")
    public @ResponseBody
    String selectAll() {
        return JSON.toJSONString(studentSerivce.selectAll());
    }
}
