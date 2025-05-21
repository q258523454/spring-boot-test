package com.inter.controller;

import com.alibaba.fastjson.JSONObject;
import com.inter.entity.Teacher;
import com.inter.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


@RestController
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @RequestMapping(value = "/insertTeacher", method = RequestMethod.GET)
    public String insertTeacher() {
        Teacher teacher = new Teacher();
        teacher.setUsername("teacher");
        teacher.setPassword("teacher");
        teacher.setRegTime(new Date());
        return Integer.toString(teacherService.insertTeacher(teacher));
    }

    @RequestMapping(value = "/selectAllTeacher", method = RequestMethod.GET)
    public String selectAllTeacher() {
        return JSONObject.toJSONString(teacherService.selectAllTeacher());
    }

}