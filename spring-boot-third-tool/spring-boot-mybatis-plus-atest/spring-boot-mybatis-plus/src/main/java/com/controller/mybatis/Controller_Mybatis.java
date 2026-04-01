package com.controller.mybatis;


import com.alibaba.fastjson.JSON;
import com.dao.StudentMapper;
import com.entity.Student;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.annotation.Resource;

@RestController
public class Controller_Mybatis {
    @Resource
    private StudentMapper studentMapper;

    @GetMapping(value = "/student/select/all")
    public String index() {
        List<Student> students = studentMapper.selectAll();
        return JSON.toJSONString(students);
    }
}
