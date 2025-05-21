package com.trans.controller;

import com.alibaba.fastjson.JSONObject;
import com.trans.entity.Student;
import com.trans.service.StudentService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;

    @RequestMapping(value = "/student/insert", method = RequestMethod.GET)
    public String insertStudent() {
        Student student = new Student();
        return "" + studentService.insertStudent(Student.createStudent("student"));
    }

    @RequestMapping(value = "/student/selectall", method = RequestMethod.GET)
    public String selectAllStudent() {
        return JSONObject.toJSONString(studentService.selectAllStudent());
    }

}