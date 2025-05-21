package com.controller.mybatisplus;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dao.StudentPlusMapper;
import com.entity.Student;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

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
    @GetMapping(value = "/mybatis/plus/select/page1/{pageNum}/{pageSize}")
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
     * mybatis-plus 内置分页
     */
    @GetMapping(value = "/mybatis/plus/select/page2/{pageNum}/{pageSize}")
    public String pageTest2(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize) {
        Page<Student> page = new Page<>(pageNum, pageSize);
        // null 表示查询全部
        Page<Student> studentPage = studentPlusMapper.selectPage(page, null);
        return JSON.toJSONString(studentPage);
    }

    /**
     * mybatis-plus 内置分页:LambdaQueryWrapper
     */
    @GetMapping(value = "/mybatis/plus/select/page3/{pageNum}/{pageSize}")
    public String pageTest3(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize) {
        Page<Student> pageParam = new Page<>(pageNum, pageSize);
        // 查询条件
        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Student::getName) // 待查询字段
                .lt(Student::getId, 3); // 查询条件
        // 分页查询,wrapper=null表示查询全部
        Page<Student> page = studentPlusMapper.selectPage(pageParam, wrapper);
        // 集合数据
        List<Student> list = page.getRecords();
        return JSON.toJSONString(page);
    }

    /**
     * mybatis-plus 内置分页:LambdaQueryChainWrapper
     */
    @GetMapping(value = "/mybatis/plus/select/page4/{pageNum}/{pageSize}")
    public String page2(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize) {
        LambdaQueryChainWrapper<Student> wrapper = new LambdaQueryChainWrapper<>(studentPlusMapper)
                .select(Student::getName) // 待查询字段
                .lt(Student::getId, 3); // 查询条件
        // isSearchCount:false表示不查询总count, new Page<>(current, size,false);
        Page<Student> page = new Page<>(pageNum, pageSize, true);
        // id 降序
        page.addOrder(OrderItem.desc("id"));
        Page<Student> studentPage = wrapper.page(page);
        // 序列化
        String jsonString = JSON.toJSONString(studentPage);
        // 反序列化
        Page<Student> serial = JSON.parseObject(jsonString, new TypeReference<Page<Student>>() {
        });
        log.info(JSON.toJSONString(serial));
        return jsonString;
    }

}
