package com.inter.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inter.dao.StudentPlusMapper;
import com.inter.entity.Student;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inter.uti.ScopeThreadUtils;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
public class Controller_Mybatis_Plus_Page {
    @Autowired
    private StudentPlusMapper studentPlusMapper;

    /**
     * mybatis 常规分页查询
     */
    @GetMapping(value = "/mybatis/select/page/{pageNum}/{pageSize}")
    public String pageTest1(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize) {
        // pageNum 查询第几页, pageSize 每页的大小(会根据select count(1) 计算总页数)
        PageInfo<Student> pageInfo = null;
        if (pageNum != null && pageSize != null) {
            // 特别注意: 只对其后的第一个mapper有效, 为保证线程安全PageHelper和dao层必须作为一个整体
            // service/mapper 紧随 PageHelper 其后
            PageHelper.startPage(pageNum, pageSize);
            List<Student> studentList = studentPlusMapper.selectAll();
            pageInfo = new PageInfo<>(studentList);
        }
        return JSON.toJSONString(pageInfo);
    }

    /**
     * mybatis 常规分页查询
     * 带scope
     * <a href="#">http://localhost:8081/mybatis/select/page/scope/1/3</a>
     */
    @GetMapping(value = "/mybatis/select/page/scope/{pageNum}/{pageSize}")
    public String scope(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize) {
        ScopeThreadUtils.setDataScope();
        // pageNum 查询第几页, pageSize 每页的大小(会根据select count(1) 计算总页数)
        PageInfo<Student> pageInfo = null;
        if (pageNum != null && pageSize != null) {
            // 特别注意: 只对其后的第一个mapper有效, 为保证线程安全PageHelper和dao层必须作为一个整体
            // service/mapper 紧随 PageHelper 其后
            PageHelper.startPage(pageNum, pageSize);
            List<Student> studentList = studentPlusMapper.selectAll();
            pageInfo = new PageInfo<>(studentList);
        }
        return JSON.toJSONString(pageInfo);
    }

    /**
     * mybatis-plus 内置分页:LambdaQueryWrapper
     */
    @GetMapping(value = "/mybatis/plus/select/page/{pageNum}/{pageSize}")
    public String pageTest3(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize) {
        Page<Student> pageParam = new Page<>(pageNum, pageSize);
        // 查询条件
        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        wrapper.select().lt(Student::getId, 5);
        // 分页查询,wrapper=null表示查询全部
        Page<Student> page = studentPlusMapper.selectPage(pageParam, wrapper);
        // 集合数据
        List<Student> list = page.getRecords();
        return JSON.toJSONString(page);
    }

    /**
     * mybatis-plus 内置分页:LambdaQueryWrapper
     * 带scope
     * <a href="#">http://localhost:8081/mybatis/plus/select/page/scope/1/3</a>
     */
    @GetMapping(value = "/mybatis/plus/select/page/scope/{pageNum}/{pageSize}")
    public String pageTest4(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize) {
        ScopeThreadUtils.setDataScope();
        Page<Student> pageParam = new Page<>(pageNum, pageSize);
        // 查询条件
        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        wrapper.select().lt(Student::getId, 5);
        // 分页查询,wrapper=null表示查询全部
        Page<Student> page = studentPlusMapper.selectPage(pageParam, wrapper);
        // 集合数据
        List<Student> list = page.getRecords();
        return JSON.toJSONString(page);
    }

}
