package com.controller.mybatisplus;


import com.alibaba.fastjson.JSON;
import com.dao.StudentPlusMapper;
import com.entity.Student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Controller_Mybatis_Plus {
    @Autowired
    private StudentPlusMapper studentPlusMapper;


    @GetMapping(value = "/mybatis/plus/select/all")
    public String index1() {
        List<Student> studentList = studentPlusMapper.selectList(null);
        return JSON.toJSONString(studentList);
    }

    /**
     * 通过传统的 mapper.xml 来查询
     */
    @GetMapping(value = "/mybatis/plus/select/all2")
    public String index2() {
        List<Student> studentList = studentPlusMapper.selectAll();
        return JSON.toJSONString(studentList);
    }

    @GetMapping(value = "/mybatis/plus/select/{id}")
    public String index3(@PathVariable("id") Integer id) {
        if (null != id) {
            Student student = studentPlusMapper.selectById(id);
            return JSON.toJSONString(student);
        }
        return null;
    }
}
