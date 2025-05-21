package com.aop.controller;


import com.aop.aspects.MySpel;
import com.aop.entity.Student;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class T4_SpelAspectCtrl {

    // 获取名为id的参数
    @MySpel(spelSingleValue = "#value", spelArray = {"#value", "#value"}, spelObjValue = "#student.age")
    @GetMapping("/spel")
    public String test(@RequestParam("value") String value, @RequestBody(required = false) Student student) {
        return "hello";
    }
}
