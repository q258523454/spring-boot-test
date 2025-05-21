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
public class Controller_Normal {

    @Autowired
    private StudentNormalService studentNormalService;

    /**
     * 默认 @Primary 事务管理器 @Transactional(value = "myNormalTransactionManager")
     * 该事务管理器下的 数据源是 normal (库名zhang)
     */
    @GetMapping(value = "/normal/student/insert")
    public String test1() {
        StudentBase student = StudentUtil.getRandomStudentBaseList(1).get(0);
        studentNormalService.save(student);
        return JSON.toJSONString(student);
    }


    /**
     * 指定用 normal 事务管理器(normal数据源,库名zhang)
     */
    @GetMapping(value = "/normal/student/insert2")
    @Transactional(value = "myNormalTransactionManager", rollbackFor = Exception.class)
    public String test2() {
        StudentBase student = StudentUtil.getRandomStudentBaseList(1).get(0);
        studentNormalService.save(student);
        return JSON.toJSONString(student);
    }

    @GetMapping(value = "/normal/student/select/all")
    public String test4() {
        List<StudentBase> studentList = studentNormalService.list(null);
        return JSON.toJSONString(studentList);
    }

}
