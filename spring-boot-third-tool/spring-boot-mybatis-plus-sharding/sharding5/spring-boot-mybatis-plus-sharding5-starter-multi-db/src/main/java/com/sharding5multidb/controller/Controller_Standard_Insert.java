package com.sharding5multidb.controller;


import com.sharding5multidb.entity.Student;
import com.sharding5multidb.service.StudentService;
import com.sharding5multidb.utils.StudentUtil;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
public class Controller_Standard_Insert {

    @Autowired
    private StudentService studentService;


    /**
     * standard 批量插入 - foreach
     */
    @GetMapping(value = "/sharding/standard/student/insert/foreach/{num}")
    @Transactional(value = "myShardingTransactionManager", rollbackFor = Exception.class)
    public String test2(@PathVariable("num") int num) {
        List<Student> studentList = StudentUtil.getRandomStudentList(num);
        int i = studentService.insertList(studentList);
        return "" + i;
    }

    /**
     * standard 批量插入 - saveBatch
     */
    @GetMapping(value = "/sharding/standard/student/insert/batch/{num}")
    @Transactional(value = "myShardingTransactionManager", rollbackFor = Exception.class)
    public String saveBatch(@PathVariable("num") int num) {
        List<Student> studentList = StudentUtil.getRandomStudentList(num);
        studentService.saveBatch(studentList);
        return "ok";
    }

    /**
     * sharding分库分表 会统一回滚
     */
    @Transactional
    @GetMapping(value = "/sharding/standard/student/insert/batch/error/{num}")
    public String error(@PathVariable("num") int num) {
        List<Student> studentList = StudentUtil.getRandomStudentList(num);
        studentService.saveBatch(studentList);
        throw new RuntimeException("error");
    }
}
