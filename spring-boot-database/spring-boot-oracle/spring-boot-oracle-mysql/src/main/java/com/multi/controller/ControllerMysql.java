package com.multi.controller;

import com.alibaba.fastjson.JSON;
import com.multi.entity.mysql.Student;
import com.multi.service.IStudentServiceMysql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @Author: zhangj
 * @Date: 2019-12-16
 * @Version 1.0
 */

@RestController
@RequestMapping("/mysql")
public class ControllerMysql {
    private static final Logger logger = LoggerFactory.getLogger(ControllerMysql.class);

    @Autowired
    private IStudentServiceMysql studentServiceMysql;


    @GetMapping(value = "/selectAll")
    public String selectAll() {
        return JSON.toJSONString(studentServiceMysql.selectAll());
    }


    @GetMapping(value = "/insert")
    public String insert(String id) {
        com.multi.entity.mysql.Student student = new Student();
        student.setId(Integer.valueOf(id));
        student.setName(UUID.randomUUID().toString().substring(0, 3));
        student.setAge(21);
        return JSON.toJSONString(studentServiceMysql.insert(student));
    }
}
