package com.oracle.batch.controller;

import com.alibaba.fastjson.JSON;
import com.oracle.batch.service.IOracleStudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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
}
