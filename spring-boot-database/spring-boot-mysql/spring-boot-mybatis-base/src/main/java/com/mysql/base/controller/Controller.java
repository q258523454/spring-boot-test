package com.mysql.base.controller;

import com.alibaba.fastjson.JSON;
import com.mysql.base.dao.StudentMapper;
import com.mysql.base.entity.Student;
import com.mysql.base.serivce.IStudentService;
import com.mysql.base.util.MybatisSqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @Author: zhangj
 * @Date: 2019-12-16
 * @Version 1.0
 */

@Slf4j
@RestController
public class Controller {
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    @Autowired
    private IStudentService studentService;
    @Autowired
    private SqlSession sqlSession;

    @GetMapping(value = "/selectAll")
    public String selectAll() {
        String sql = MybatisSqlUtil.showSql(sqlSession, StudentMapper.class, "selectAll", new Object[]{""});
        log.warn(sql);
        String sql2 = MybatisSqlUtil.showSql(sqlSession, StudentMapper.class, "insert", new Object[]{""});
        log.warn(sql2);
        String sql3 = MybatisSqlUtil.showSql(sqlSession, StudentMapper.class, "updateByName", new Object[]{new Student(), ""});
        log.warn(sql3);
        return JSON.toJSONString(studentService.selectAll());
    }


    @GetMapping(value = "/insert")
    public String insert(String id) throws InterruptedException {
        Student student = getStudent("");
        return JSON.toJSONString(studentService.insert(student));
    }

    @GetMapping(value = "/update")
    public String update(String name) {
        Student student = new Student();
        student.setName(name);
        student.setAge(99);
        return JSON.toJSONString(studentService.updateByName(student, name));
    }


    public Student getStudent(String id) {
        String wId = "";
        if (null != id && !id.isEmpty()) {
            wId = id;
        } else {
            wId = String.valueOf(studentService.selectMaxId() + 1);
        }
        Student student = new Student();
        student.setId(Integer.valueOf(wId));
        student.setName(UUID.randomUUID().toString().substring(0, 3));
        student.setAge(Integer.valueOf(("20")));
        return student;
    }
}
