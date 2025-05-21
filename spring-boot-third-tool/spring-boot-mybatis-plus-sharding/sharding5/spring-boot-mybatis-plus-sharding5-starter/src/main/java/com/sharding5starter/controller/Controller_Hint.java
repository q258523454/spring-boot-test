package com.sharding5starter.controller;


import com.alibaba.fastjson.JSON;
import com.sharding5starter.entity.Student;
import com.sharding5starter.service.StudentService;
import com.utils.StudentUtil;

import lombok.extern.slf4j.Slf4j;

import org.apache.shardingsphere.infra.hint.HintManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class Controller_Hint {

    @Autowired
    private StudentService studentService;

    @GetMapping(value = "/sharding/hint/student/insert")
    @Transactional(rollbackFor = Exception.class)
    public String test1() {
        HintManager.clear();
        HintManager hintManager = HintManager.getInstance();
        hintManager.addDatabaseShardingValue("student", null);
        hintManager.addDatabaseShardingValue("student", 1);
        hintManager.addTableShardingValue("student", 0);
        hintManager.addTableShardingValue("student", 1);
        Student student = StudentUtil.getRandomStudentList(1).get(0);
        studentService.getBaseMapper().insert(student);
        HintManager.clear();
        return JSON.toJSONString(student);
    }
}
