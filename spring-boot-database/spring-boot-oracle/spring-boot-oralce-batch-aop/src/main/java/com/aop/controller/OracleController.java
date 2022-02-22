package com.aop.controller;

import com.alibaba.fastjson.JSON;
import com.aop.entity.Student;
import com.aop.service.IOracleStudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * @Author: zhangj
 * @Date: 2019-12-16
 * @Version 1.0
 */

@RestController
public class OracleController {
    private static final Logger logger = LoggerFactory.getLogger(OracleController.class);

    @Autowired
    private IOracleStudentService studentService;


    @GetMapping(value = "/student/selectall")
    public String selectAll() throws IOException {
        return JSON.toJSONString(studentService.selectAll());
    }


    @GetMapping(value = "/student/insert")
    public String insert(String id) {
        String wId = "";
        if (null != id && !id.isEmpty()) {
            wId = id;
        } else {
            wId = String.valueOf(studentService.getMaxId() + 1);
        }
        Student student = new Student();
        student.setId(new BigDecimal(wId));
        student.setName(UUID.randomUUID().toString().substring(0, 3));
        student.setAge(new BigDecimal("20"));
        int insert = studentService.insert(student);
        logger.info("Mybatis SQL return :" + insert);
        return JSON.toJSONString(insert);
    }


    @GetMapping(value = "/update")
    public String update(String name) {
        Student student = new Student();
        student.setName(name);
        student.setAge(BigDecimal.valueOf(99));
        int i = studentService.updateByName(student, name);
        logger.info("Mybatis SQL return :" + i);
        return JSON.toJSONString(i);
    }


}
