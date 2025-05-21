package com.inter.controller;

import com.alibaba.fastjson.JSON;
import com.inter.entity.Student;
import com.inter.service.StudentCacheSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Date: 2019-05-10
 * @Version 1.0
 */

@RestController
public class Contoller {

    @Autowired
    private StudentCacheSerivce studentCacheSerivce;


    @PostMapping(value = "/insertStudent", produces = "application/json; charset=UTF-8")
    public String insertStudent(Student student) {
        return studentCacheSerivce.insertStudent(student) + "";
    }

    @DeleteMapping(value = "/deleteStudentById/{id}", produces = "application/json; charset=UTF-8")
    public String deleteStudentById(@PathVariable("id") Integer id) {
        studentCacheSerivce.deleteStudentById(id);
        return "success to delete =" + id.toString();
    }

    @PutMapping(value = "/updateStudent", produces = "application/json; charset=UTF-8")
    public String updateStudent(Student student) {
        return JSON.toJSONString(studentCacheSerivce.updateStudent(student));
    }

    @GetMapping(value = "/selectStudentById/{id}", produces = "application/json; charset=UTF-8")
    public String selectStudentById(@PathVariable("id") Integer id) {
        Student student = studentCacheSerivce.selectStudentById(id);
        return JSON.toJSONString(student);
    }

    @GetMapping(value = "/selectAllStudent", produces = "application/json; charset=UTF-8")
    public String selectAllStudent() {
        List<Student> student = studentCacheSerivce.selectAllStudent();
        return JSON.toJSONString(student);
    }

}
