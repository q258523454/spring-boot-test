package com.multi.controller;

import com.alibaba.fastjson.JSON;
import com.multi.entity.oracle.Student;
import com.multi.service.IStudentServiceOracle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @Author: zhangj
 * @Date: 2019-12-16
 * @Version 1.0
 */

@RestController
@RequestMapping("/oracle")
public class ControllerOracle {
    private static final Logger logger = LoggerFactory.getLogger(ControllerOracle.class);

    @Autowired
    private IStudentServiceOracle studentService;


    @GetMapping(value = "/selectAll")
    public String selectAll() {
        return JSON.toJSONString(studentService.selectAll());
    }


    @GetMapping(value = "/insert")
    public String insert(String id) {
        Student student = new Student();
        student.setId(new BigDecimal(id));
        student.setName(UUID.randomUUID().toString().substring(0, 3));
        student.setAge(new BigDecimal("20"));
        return JSON.toJSONString(studentService.insert(student));
    }
}
