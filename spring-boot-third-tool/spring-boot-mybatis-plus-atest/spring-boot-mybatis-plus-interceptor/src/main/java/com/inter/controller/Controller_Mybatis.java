package com.inter.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.inter.dao.StudentPlusMapper;
import com.inter.entity.Student;
import com.inter.uti.ScopeThreadUtils;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Controller_Mybatis {
    @Autowired
    private StudentPlusMapper studentPlusMapper;

    @GetMapping(value = "/mybatis/plus/select/all")
    public String index1() {
        List<Student> studentList = studentPlusMapper.selectList(null);
        return JSON.toJSONString(studentList);
    }

    /**
     * scope数据权限增强
     */
    @GetMapping(value = "/mybatis/plus/select")
    public String index3(@Param("id") String id) {
        if (StringUtils.isEmpty(id)) {
            throw new RuntimeException("id不能为空");
        }
        ScopeThreadUtils.setDataScope();
        List<Student> studentList = studentPlusMapper.selectById(id);
        return JSON.toJSONString(studentList);
    }


    @GetMapping(value = "/mybatis/plus/select/wrapper")
    public String wrapper1() {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        // 所有 年龄=18 数据
        queryWrapper.eq("age", 18);
        List<Student> studentList = studentPlusMapper.selectList(queryWrapper);
        return JSON.toJSONString(studentList);
    }
}
