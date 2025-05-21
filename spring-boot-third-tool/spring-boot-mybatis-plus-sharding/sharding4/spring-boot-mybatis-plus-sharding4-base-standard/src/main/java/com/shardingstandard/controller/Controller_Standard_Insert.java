package com.shardingstandard.controller;


import com.alibaba.fastjson.JSON;
import com.shardingstandard.entity.Student;
import com.shardingstandard.service.StudentService;
import com.shardingstandard.utils.StudentUtil;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
public class Controller_Standard_Insert {

    @Autowired
    private StudentService studentService;

    /**
     * 单条插入
     * 不同的id会走不同的库和表
     */
    @GetMapping(value = "/sharding/standard/student/insert")
    public String test1() {
        List<Student> randomStudentList = StudentUtil.getRandomStudentList(1);
        Student student = randomStudentList.get(0);
        studentService.getBaseMapper().insert(student);
        return JSON.toJSONString(student);
    }

    /**
     * 批量插入 - foreach
     * mysql:begin end 只能用在存储过程
     */
    @GetMapping(value = "/sharding/student/insert/foreach")
    public String test2() {
        List<Student> studentList = StudentUtil.getRandomStudentList(5);
        int i = studentService.insertList(studentList);
        return "" + i;
    }

}
