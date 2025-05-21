package com.shardingstandard.controller;


import com.alibaba.fastjson.JSON;
import com.shardingstandard.entity.Student;
import com.shardingstandard.service.StudentService;
import com.shardingstandard.utils.StudentUtil;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
public class Controller_Standard_Transaction {

    @Autowired
    private StudentService studentService;


    /**
     * mybatis plus 批量插入
     */
    @Transactional
    @GetMapping(value = "/sharding/standard/student/insert/batch/{num}")
    public String test1(@PathVariable("num") int num) {
        List<Student> studentList = StudentUtil.getRandomStudentList(num);
        studentService.saveBatch(studentList);
        return JSON.toJSONString("ok");
    }

    /**
     * sharding分库分表 会统一回滚
     */
    @Transactional
    @GetMapping(value = "/sharding/standard/student/insert/batch/error/{num}")
    public String test2(@PathVariable("num") int num) {
        List<Student> studentList = StudentUtil.getRandomStudentList(num);
        studentService.saveBatch(studentList);
        throw new RuntimeException("error");
    }
}
