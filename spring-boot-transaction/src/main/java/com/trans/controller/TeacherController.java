package com.trans.controller;

import com.alibaba.fastjson.JSONObject;
import com.trans.entity.Teacher;
import com.trans.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


@RestController
public class TeacherController {

    private Logger log = LoggerFactory.getLogger(TeacherController.class);

    @Autowired
    private TeacherService teacherService;

    @RequestMapping(value = "/teacher/insert", method = RequestMethod.GET)
    public String insertTeacher() {
        Teacher teacher = new Teacher();
        teacher.setUsername("teacher");
        teacher.setPassword("teacher");
        teacher.setRegTime(new Date());
        return "" + teacherService.insertTeacher(teacher);
    }

    @RequestMapping(value = "/teacher/selectall", method = RequestMethod.GET)
    public String selectAllTeacher() {
        return JSONObject.toJSONString(teacherService.selectAllTeacher());
    }

}