package com.multidb.controller.normal;


import com.alibaba.fastjson.JSON;
import com.multidb.controller.StudentUtil;
import com.multidb.entity.StudentBase;
import com.multidb.service.normal.StudentNormalService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
public class Controller_Normal_Transaction {

    @Autowired
    private StudentNormalService studentNormalService;


    @GetMapping(value = "/normal/student/insert/batch")
    @Transactional(value = "myNormalTransactionManager", rollbackFor = Exception.class)
    public String test1() {
        List<StudentBase> studentList = StudentUtil.getRandomStudentBaseList(5);
        studentNormalService.saveBatch(studentList);
        return JSON.toJSONString("ok");
    }

    @GetMapping(value = "/normal/student/insert/batch/foreach")
    @Transactional(value = "myNormalTransactionManager", rollbackFor = Exception.class)
    public String test11() {
        List<StudentBase> studentList = StudentUtil.getRandomStudentBaseList(5);
        int i = studentNormalService.insertList(studentList);
        return JSON.toJSONString("ok");
    }

    @GetMapping(value = "/normal/student/insert/batch/error")
    @Transactional(value = "myNormalTransactionManager", rollbackFor = Exception.class)
    public String test2() {
        List<StudentBase> studentList = StudentUtil.getRandomStudentBaseList(5);
        studentNormalService.saveBatch(studentList);
        throw new RuntimeException("error");
    }

}
