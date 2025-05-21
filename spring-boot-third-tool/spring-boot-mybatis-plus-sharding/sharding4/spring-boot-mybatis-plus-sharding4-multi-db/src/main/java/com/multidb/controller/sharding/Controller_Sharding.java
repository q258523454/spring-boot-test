package com.multidb.controller.sharding;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.multidb.controller.StudentUtil;
import com.multidb.entity.Student;
import com.multidb.service.sharding.StudentShardingService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
public class Controller_Sharding {

    @Autowired
    private StudentShardingService studentShardingService;

    /**
     * 不同的id会走不同的库和表
     */
    @GetMapping(value = "/sharding/student/insert")
    @Transactional(value = "myShardingTransactionManager", rollbackFor = Exception.class)
    public String test1() {
        Student student = StudentUtil.getRandomStudentList(1).get(0);
        studentShardingService.getBaseMapper().insert(student);
        return JSON.toJSONString(student);
    }

    /**
     * 不同的id会走不同的库和表
     * eg:
     * id=0: Actual SQL: db0 ::: SELECT  id,name,age  FROM student_0
     * id=1: Actual SQL: db1 ::: SELECT  id,name,age  FROM student_1
     */
    @GetMapping(value = "/sharding/student/select/{id}")
    @Transactional(value = "myShardingTransactionManager", rollbackFor = Exception.class)
    public String test2(@PathVariable("id") Long id) {
        LambdaQueryWrapper<Student> studentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<Student> studentList = studentShardingService.getBaseMapper()
                .selectList(studentLambdaQueryWrapper.eq(Student::getId, id));
        return JSON.toJSONString(studentList);
    }


    /**
     * 注意:查询全部的时候,每个分库都要有所有的分表, 库 zhang_0 和 zhang_1 都要有{student_0,student_1}
     */
    @GetMapping(value = "/sharding/student/select/all")
    @Transactional(value = "myShardingTransactionManager", rollbackFor = Exception.class)
    public String test2() {
        List<Student> studentList = studentShardingService.getBaseMapper().selectList(null);
        return JSON.toJSONString(studentList);
    }
}
