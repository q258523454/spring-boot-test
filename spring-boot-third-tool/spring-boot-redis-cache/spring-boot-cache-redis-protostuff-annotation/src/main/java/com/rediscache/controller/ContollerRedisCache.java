package com.rediscache.controller;

import com.alibaba.fastjson.JSON;
import com.rediscache.entity.Student;
import com.rediscache.service.StudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Date: 2019-05-10
 * @Version 1.0
 */

@RestController
public class ContollerRedisCache {

    @Autowired
    private StudentService studentService;

    @PostMapping(value = "/insertStudent", produces = "application/json; charset=UTF-8")
    public String insertStudent(Student student) {
        return studentService.insertStudent(student) + "";
    }

    @DeleteMapping(value = "/deleteStudentById/{id}", produces = "application/json; charset=UTF-8")
    public String deleteStudentById(@PathVariable("id") Integer id) {
        studentService.deleteStudentById(id);
        return "success to delete =" + id.toString();
    }

    @PutMapping(value = "/updateStudent", produces = "application/json; charset=UTF-8")
    public String updateStudent(Student student) {
        return JSON.toJSONString(studentService.updateStudent(student));
    }

    @GetMapping(value = "/selectStudentById/{id}", produces = "application/json; charset=UTF-8")
    public String selectStudentById(@PathVariable("id") Integer id) {
        Student student = studentService.selectStudentById(id);
        return JSON.toJSONString(student);
    }

}
