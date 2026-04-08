package com.controller.mybatis;


import com.alibaba.fastjson.JSON;
import com.dao.StudentMapper;
import com.entity.Student;
import com.service.StudentService;

import jakarta.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class Controller_Mybatis {
    @Resource
    private StudentService studentService;

    @GetMapping(value = "/student/select/all")
    public String index() {
        List<Student> students = studentService.selectAll();
        return JSON.toJSONString(students);
    }
}
