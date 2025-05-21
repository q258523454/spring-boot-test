package com.handler.controller;

import com.alibaba.fastjson.JSON;
import com.handler.entity.Student;
import com.handler.serivce.IStudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;



@RestController
public class Controller {
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    @Autowired
    private IStudentService studentService;


    @GetMapping(value = "/selectAll")
    public String selectAll() {
        return JSON.toJSONString(studentService.selectAll());
    }


    @GetMapping(value = "/insert")
    public String insert(String id) {
        Student student = new Student();
        student.setId(Integer.valueOf(id));
        student.setName(UUID.randomUUID().toString().substring(0, 3));
        student.setAge(21);
        return JSON.toJSONString(studentService.insert(student));
    }

    @GetMapping(value = "/update")
    public String update(String name) {
        Student student = new Student();
        student.setName(name);
        student.setAge(99);
        return JSON.toJSONString(studentService.updateByName(student, name));
    }
}
