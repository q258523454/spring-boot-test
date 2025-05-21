package com.controller.mybatisplus;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.entity.Student;
import com.service.StudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RestController
public class Controller_Wrapper_Select {
    @Autowired
    private StudentService studentService;


    @GetMapping(value = "/mybatis/plus/select/wrapper1")
    @Transactional
    public String wrapper1() {
        Student student = Student.builder().id(new Random().nextInt(100)).name("123").age(18).build();
        studentService.getBaseMapper().insert(student);
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        // 所有 年龄=18 数据
        queryWrapper.eq("age", 18);
        List<Student> studentList = studentService.selectList(student);
        return JSON.toJSONString(studentList);
    }

    /**
     * in 测试
     */
    @GetMapping(value = "/mybatis/plus/select/wrapper2")
    public String wrapper12() {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        // 所有 年龄=18,19,20 数据
        queryWrapper.in("age", Arrays.asList(18, 19, 20));
        List<Student> studentList = studentService.getBaseMapper().selectList(queryWrapper);
        return JSON.toJSONString(studentList);
    }

    /**
     * 大于
     */
    @GetMapping(value = "/mybatis/plus/select/wrapper/gt")
    public String wrapperGt() {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        // 姓 wang, 年龄大于 18, 按年龄降序
        queryWrapper.likeRight("name", "wang")
                .gt("age", 18)
                .orderByDesc("age");
        List<Student> studentList = studentService.getBaseMapper().selectList(queryWrapper);
        return JSON.toJSONString(studentList);
    }

    /**
     * 小于
     */
    @GetMapping(value = "/mybatis/plus/select/wrapper/lt")
    public String wrapperLt() {
        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        wrapper.lt(Student::getCreatedAt, new Date());
        List<Student> studentList = studentService.selectList(wrapper);
        return JSON.toJSONString(studentList);
    }

    /**
     * between
     */
    @GetMapping(value = "/mybatis/plus/select/wrapper/between")
    public String wrapperbetween() {
        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        Date cur = new Date();
        // 过去24小时
        long last24HoursMill = cur.getTime() - 24 * 60 * 60 * 1000L;
        // 过去30分钟
        long last30MinutesMill = cur.getTime() - 30 * 60 * 1000L;
        Date left = new Date(last30MinutesMill);
        wrapper.between(Student::getCreatedAt, left, cur);
        List<Student> studentList = studentService.selectList(wrapper);
        return JSON.toJSONString(studentList);
    }


    /**
     * groupBy
     */
    @GetMapping(value = "/mybatis/plus/select/wrapper/groupby")
    public String wrapper4() {
        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Student::getId).groupBy(Student::getName);
        List<Student> studentList = studentService.selectList(wrapper);
        return JSON.toJSONString(studentList);
    }

    /**
     * like 模糊查询1
     */
    @GetMapping(value = "/mybatis/plus/select/wrapper5")
    public String wrapper5() {
        LambdaQueryWrapper<Student> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // like 会在左右两边加 百分号%
        lambdaQueryWrapper.like(Student::getName, "wang").gt(Student::getAge, 18);
        List<Student> studentList = studentService.selectList(lambdaQueryWrapper);
        return JSON.toJSONString(studentList);
    }

    /**
     * like 模糊查询2
     */
    @GetMapping(value = "/mybatis/plus/select/wrapper6")
    public String wrapper6() {
        LambdaQueryWrapper<Student> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.likeRight(Student::getName, "wang")
                .and(sub -> sub.gt(Student::getAge, 18).or().lt(Student::getAge, 10));
        List<Student> studentList = studentService.selectList(lambdaQueryWrapper);
        return JSON.toJSONString(studentList);
    }

    /**
     * Lambda selective 查询
     * flag: true 则按指定条件查询
     * flag: false 查询全部
     */
    @GetMapping(value = "/mybatis/plus/select/wrapper7/{flag}")
    public String wrapper7(@PathVariable("flag") boolean flag) {
        LambdaQueryWrapper<Student> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(flag, Student::getName, "zhangsan");
        List<Student> studentList = studentService.selectList(lambdaQueryWrapper);
        return JSON.toJSONString(studentList);
    }
}
