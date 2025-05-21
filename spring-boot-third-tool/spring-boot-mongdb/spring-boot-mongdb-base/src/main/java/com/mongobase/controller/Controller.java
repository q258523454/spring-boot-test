package com.mongobase.controller;

import com.alibaba.fastjson.JSON;
import com.mongobase.pojo.entity.Student;
import com.mongobase.service.StudentService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
public class Controller {

    @Autowired
    private StudentService studentService;


    @GetMapping("/select/all")
    public String test() {
        List<Student> studentList = studentService.selectAll();
        log.info(JSON.toJSONString(studentList));
        return "ok";
    }

    @GetMapping("/insert")
    public String insert() {
        Long maxSid = studentService.getMaxSid();
        Student student = Student.createStudent(maxSid + 1, "张三");
        studentService.insertOne(student);
        log.info("insert");
        return "insert";
    }

    @GetMapping("/insert/all")
    public String insertAll() {
        Long maxSid = studentService.getMaxSid();
        List<Student> list = new ArrayList<>();
        list.add(Student.createStudent(maxSid + 1, "张三"));
        list.add(Student.createStudent(maxSid + 2, "李四"));
        list.add(Student.createStudent(maxSid + 3, "王五"));
        studentService.insertAll(list);
        log.info("all");
        return "all";
    }
}
