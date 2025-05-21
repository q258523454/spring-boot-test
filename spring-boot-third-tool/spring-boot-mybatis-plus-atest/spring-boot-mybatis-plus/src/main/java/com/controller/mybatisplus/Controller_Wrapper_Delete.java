package com.controller.mybatisplus;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.entity.Student;
import com.service.StudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
public class Controller_Wrapper_Delete {
    @Autowired
    private StudentService studentService;

    /**
     * Lambda and 查询
     */
    @GetMapping(value = "/mybatis/plus/delete/wrapper1")
    public String wrapper1() {
        LambdaQueryWrapper<Student> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Student::getAge, 3);
        boolean remove = studentService.remove(lambdaQueryWrapper);
        return remove + "";
    }

    @GetMapping(value = "/mybatis/plus/delete/batch")
    public String deleteBatch() {
        List<Integer> idList = new ArrayList<Integer>() {{
            add(2);
            add(3);
            add(4);
        }};
        LambdaQueryWrapper<Student> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Student::getAge, idList);
        boolean remove = studentService.remove(lambdaQueryWrapper);
        return remove + "";
    }
}
