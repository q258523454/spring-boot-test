package com.sharding5starter.controller;


import com.alibaba.fastjson.JSON;
import com.sharding5starter.entity.Student;
import com.sharding5starter.service.StudentService;
import com.utils.StudentUtil;

import lombok.extern.slf4j.Slf4j;

import org.apache.shardingsphere.infra.hint.HintManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
public class Controller_Insert {

    @Autowired
    private StudentService studentService;

    @GetMapping(value = "/index")
    public String index() {
        return "index";
    }


    /**
     * 单条插入
     * 不同的id会走不同的库和表
     */
    @GetMapping(value = "/sharding/student/insert")
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


    @GetMapping(value = "/sharding/student/insert2")
    @Transactional(rollbackFor = Exception.class)
    public String test3() {
        HintManager.clear();
        HintManager hintManager = HintManager.getInstance();
        hintManager.addTableShardingValue("student", 0);
        Student student = StudentUtil.getRandomStudentList(1).get(0);
        studentService.getBaseMapper().insert(student);
        HintManager.clear();
        return JSON.toJSONString(student);
    }

    @GetMapping(value = "/sharding/student/insert/foreach2")
    @Transactional(rollbackFor = Exception.class)
    public String test4() {
        HintManager.clear();
        HintManager hintManager = HintManager.getInstance();
        hintManager.addTableShardingValue("student", 0);
        List<Student> studentList = StudentUtil.getRandomStudentList(5);
        int i = studentService.insertList(studentList);
        HintManager.clear();
        return JSON.toJSONString(i);
    }
}
