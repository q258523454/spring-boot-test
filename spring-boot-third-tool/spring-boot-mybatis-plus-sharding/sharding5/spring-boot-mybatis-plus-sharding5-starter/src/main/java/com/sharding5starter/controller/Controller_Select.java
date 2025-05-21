package com.sharding5starter.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sharding5starter.entity.Student;
import com.sharding5starter.service.StudentService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
public class Controller_Select {

    @Autowired
    private StudentService studentService;

    @GetMapping(value = "/sharding/student/select/{id}")
    public String test2(@PathVariable("id") Long id) {
        LambdaQueryWrapper<Student> studentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<Student> studentList = studentService.getBaseMapper()
                .selectList(studentLambdaQueryWrapper.eq(Student::getId, id));
        return JSON.toJSONString(studentList);
    }

    @GetMapping(value = "/sharding/student/select/all")
    public String test2() {
        List<Student> studentList = studentService.getBaseMapper().selectList(null);
        return JSON.toJSONString(studentList);
    }
}
