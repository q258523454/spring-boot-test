package com.controller.mybatisplus;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.entity.Student;
import com.service.StudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller_Wrapper_Select_One {

    @Autowired
    private StudentService studentService;


    /**
     * getOne 默认
     * 查询数量>1 抛异常
     * 查询数量=1 or null 正常返回
     */
    @GetMapping(value = "/mybatis/plus/select/getone")
    public String wrapper1() {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        // 所有 年龄=18 数据
        queryWrapper.eq("age", 18);
        Student one = studentService.getOne(queryWrapper);
        return JSON.toJSONString(one);
    }

    /**
     * getOne 不抛异常, 多个则返回第一个 get(0)
     */
    @GetMapping(value = "/mybatis/plus/select/getone2")
    public String wrapper2() {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        // 所有 年龄=18 数据
        queryWrapper.eq("age", 18);
        Student one = studentService.getOne(queryWrapper, false);
        return JSON.toJSONString(one);
    }

    /**
     * selectOne
     * 查询数量>1 抛异常
     * 查询数量=1 or null 正常返回
     */
    @GetMapping(value = "/mybatis/plus/select/selectOne")
    public String wrapper3() {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        // 所有 年龄=18 数据
        queryWrapper.eq("age", 18);
        Student one = studentService.getBaseMapper().selectOne(queryWrapper);
        return JSON.toJSONString(one);
    }
}
