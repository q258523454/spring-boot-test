package com.shardingstandard.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shardingstandard.entity.Student;
import com.shardingstandard.service.StudentService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
public class Controller_Standard_Range {

    @Autowired
    private StudentService studentService;

    /**
     * 如果配置了 Range 策略, 会走对应的策略
     * 如果只配置 precise 策略 , 则按照 全库、全表 逐一路由执行全部
     */
    @GetMapping(value = "/sharding/standard/student/range/between")
    public String test2() {
        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        wrapper.between(Student::getId, 1, 10000);
        List<Student> studentList = studentService.getBaseMapper().selectList(wrapper);
        return JSON.toJSONString(studentList);
    }
}
