package com.multidb.controller.sharding;


import com.alibaba.fastjson.JSON;
import com.multidb.controller.StudentUtil;
import com.multidb.entity.Student;
import com.multidb.service.sharding.StudentShardingService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
public class Controller_Sharding_Transaction {

    @Autowired
    private StudentShardingService studentShardingService;


    /**
     * mybatisplus 批量插入 (分库分表插入正常)
     */
    @GetMapping(value = "/sharding/student/insert/batch")
    @Transactional(value = "myShardingTransactionManager", rollbackFor = Exception.class)
    public String test1() {
        List<Student> studentList = StudentUtil.getRandomStudentList(5);
        studentShardingService.saveBatch(studentList);
        return JSON.toJSONString("ok");
    }

    /**
     * mybatisplus 事务全部回滚
     */
    @GetMapping(value = "/sharding/student/insert/batch/error")
    @Transactional(value = "myShardingTransactionManager", rollbackFor = Exception.class)
    public String test2() {
        List<Student> studentList = StudentUtil.getRandomStudentList(5);
        studentShardingService.saveBatch(studentList);
        throw new RuntimeException("error");
    }


    /**
     * xml foreach 批量插入 (分库分表插入正常)
     * sharding-jdbc 会根据分库分表规则, 把SQL分类, 按不同的库执行
     */
    @GetMapping(value = "/sharding/student/xml/insert/list")
    @Transactional(value = "myShardingTransactionManager", rollbackFor = Exception.class)
    public String test11() {
        List<Student> studentList = StudentUtil.getRandomStudentList(5);
        int i = studentShardingService.insertList(studentList);
        return JSON.toJSONString("ok");
    }

    /**
     * xml 批量插入 事务全部回滚
     */
    @GetMapping(value = "/sharding/student/insert/xml/list/error")
    @Transactional(value = "myShardingTransactionManager", rollbackFor = Exception.class)
    public String test22() {
        List<Student> studentList = StudentUtil.getRandomStudentList(5);
        studentShardingService.insertList(studentList);
        throw new RuntimeException("error");
    }
}
