package com.trans.controller;

import com.alibaba.fastjson.JSONObject;
import com.trans.entity.Student;
import com.trans.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


@RestController
public class StudentController {

    private Logger log = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentService studentService;

    @RequestMapping(value = "/student/insert", method = RequestMethod.GET)
    public String insertStudent() {
        Student student = new Student();
        student.setUsername("student");
        student.setPassword("student");
        student.setRegTime(new Date());
        return "" + studentService.insertStudent(student);
    }

    @RequestMapping(value = "/student/selectall", method = RequestMethod.GET)
    public String selectAllStudent() {
        return JSONObject.toJSONString(studentService.selectAllStudent());
    }

}