package com.sharding5multidb.controller;


import com.alibaba.fastjson.JSON;
import com.sharding5multidb.entity.Student;
import com.sharding5multidb.service.StudentService;
import com.sharding5multidb.utils.StudentUtil;

import lombok.extern.slf4j.Slf4j;

import org.apache.shardingsphere.infra.hint.HintManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class Controller_Hint_Insert {

    @Autowired
    private StudentService studentService;

    /**
     * hint 插入测试
     * 注意: hint 和 standard 不能同时配置
     */
    @GetMapping(value = "/sharding/hint/student/insert")
    @Transactional(value = "myShardingTransactionManager", rollbackFor = Exception.class)
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
