package com.redis.controller;

import com.alibaba.fastjson.JSONObject;
import com.redis.entity.Student;
import com.redis.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Created By
 *
 * @author :   zhangjian
 * @date :   2018-08-28
 */

@RestController
public class Controller_Student {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StudentService studentService;

    @PostMapping(value = "/insertStudent", produces = "application/json; charset=UTF-8")
    public String insertStudent(@Validated Student student, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JSONObject.toJSONString(bindingResult.getFieldError().getDefaultMessage());
        }
        if (student.getAge() == null || student.getAge().equals("")) {
            student.setAge(Integer.toString(18 + (int) (Math.random() * (100 - 18) + 1)));
        }
        return studentService.insertStudent(student) == null ? "插入失败" : JSONObject.toJSONString(student);
    }


    // 删除
    @DeleteMapping(value = "/deleteStundetById/{id}")
    public String deleteStundetById(@PathVariable("id") Integer id) {
        return studentService.deleteStundetById(id) + "";
    }

    // 改
    @PutMapping(value = "/updateStudent/{id}")
    public String updateStudent(@PathVariable("id") Integer stuId, @Validated Student student) {
        log.info("stuId=" + stuId);
        log.info("student info: " + JSONObject.toJSONString(student));
        return studentService.updateStudent(student) + "";
    }

    // 查
    @GetMapping(value = "/selectStudentById/{id}")
    public Student selectStudentById(@PathVariable("id") Integer id) {
        return studentService.selectStudentById(id);
    }

    // 查全部
    @GetMapping(value = "/selectAllStuddent")
    public String selectAllStuddent() {
        return JSONObject.toJSONString(studentService.selectAllStuddent());
    }
}
