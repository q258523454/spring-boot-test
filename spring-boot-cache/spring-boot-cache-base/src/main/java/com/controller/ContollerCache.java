package com.controller;

import com.alibaba.fastjson.JSON;
import com.entity.Student;
import com.service.StudentCacheSerivce;
import com.service.StudentSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Date: 2019-05-10
 * @Version 1.0
 */

@RestController
public class ContollerCache {

    @Autowired
    private StudentCacheSerivce studentCacheSerivce;

    @PostMapping(value = "/insertStudentCache", produces = "application/json; charset=UTF-8")
    public String insertStudent(Student student) {
        return studentCacheSerivce.insertStudent(student) + "";
    }


    @DeleteMapping(value = "/deleteStudentByIdCache/{id}", produces = "application/json; charset=UTF-8")
    public String deleteStudentById(@PathVariable("id") Integer id) {
        studentCacheSerivce.deleteStudentById(id);
        return "success to delete =" + id.toString();
    }

    @PutMapping(value = "/updateStudentCache", produces = "application/json; charset=UTF-8")
    public String updateStudent(Student student) {
        return JSON.toJSONString(studentCacheSerivce.updateStudent(student));
    }

    @GetMapping(value = "/selectStudentByIdCache/{id}", produces = "application/json; charset=UTF-8")
    public String selectStudentById(@PathVariable("id") Integer id) {
        Student student = studentCacheSerivce.selectStudentById(id);
        return JSON.toJSONString(student);
    }


}
