package com.controller.mybatisplus;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.entity.Student;
import com.service.StudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
public class Controller_Wrapper_Update {
    @Autowired
    private StudentService studentService;


    /**
     * 批量更新测试
     * 不存在的id, 不会更新, 也不会报错;
     * 只更新输入不为null的字段, 不传的字段不更新—— updateSelective
     * 注意：
     *      1.基本类型数据(传null)会更新成默认值
     *      2.updateTime(ON UPDATE CURRENT_TIMESTAMP) 如果传入不为null，则优先使用传入值
     */
    @GetMapping(value = "/mybatis/plus/update/batch")
    public String updateBatch1() {
        List<Student> list = new ArrayList<>();

        // 只更新传入的字段, 注意：基本类型会更新为默认值(age=0)
        list.add(Student.builder().id(1).name(getRandomName()).createdAt(new Date()).build());

        // 不传的值不会更新, 注意：基本类型会更新为默认值(age=0)
        list.add(Student.builder().id(2).name(getRandomName()).build());

        // 如果updateTime(ON UPDATE CURRENT_TIMESTAMP) 字段不为空, 则不会自动更新时间
        LambdaQueryWrapper<Student> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Student::getId, 3);
        Student student = studentService.getBaseMapper().selectOne(lambdaQueryWrapper);
        student.setName(getRandomName());
        list.add(student);
        return studentService.updateBatchById(list) + "";
    }

    private String getRandomName() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
