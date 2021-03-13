package com.oracle.base.controller;

import com.oracle.base.entity.Student;
import com.oracle.base.service.IOracleStudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Author: zhangj
 * @Date: 2019-12-16
 * @Version 1.0
 */

@RestController
public class OracleControllerAtomic {
    private static final Logger logger = LoggerFactory.getLogger(OracleControllerAtomic.class);

    @Autowired
    private IOracleStudentService studentService;


    /**
     * --------------------------------------------------------------------------------------------------
     * SQL操作原子性总结:
     * 1. 没有事务, 可以部分成功.
     * 2. 没有事务, 但是 单条SQL 本身是原子操作, 要么全部成功,要么全部失败
     * 3. 没有事务, 但是 SQL 是用 BEGIN/END 组装 , 本身具有事务性
     * 4. 有事务管理, 要么全部成功, 要么全部失败
     * --------------------------------------------------------------------------------------------------
     */


    /**
     * 没有事务, 可以部分成功
     */
    @GetMapping(value = "/student/insert/for")
    public String insertList(String id) {
        long maxId = studentService.getMaxId() + 1;
        for (int i = 0; i < 2; i++) {
            long pId = maxId + i;
            Student student = getStudent(pId + "");
            studentService.insert(student);
        }
        if (null != id && !id.isEmpty()) {
            Student student = getStudent(id + "");
            studentService.insert(student);
        }
        return "ok";
    }


    /**
     * 没有事务, 但是 单条SQL 本身是原子操作, 要么全部成功,要么全部失败
     * 加不加 @Transactional 效果是一样的
     */
    @GetMapping(value = "/student/insert/foreach")
    public String insertList2(String id) {
        List<Student> studentList = new ArrayList<>();
        long maxId = studentService.getMaxId() + 1;
        for (int i = 0; i < 2; i++) {
            long pId = maxId + i;
            Student student = getStudent(pId + "");
            studentList.add(student);
        }
        if (null != id && !id.isEmpty()) {
            Student student = getStudent(id + "");
            studentList.add(student);
        }
        studentService.insertListBatch(studentList);
        return "ok";
    }

    /**
     * 没有事务, 但是 SQL 是用 BEGIN/END 组装 , 本身具有事务性
     * 加不加 @Transactional 效果是一样的
     */
    @GetMapping(value = "/student/update/beginend")
    public String updateList(String id) {
        List<Student> studentList = studentService.selectAll();
        List<Student> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Student student = getStudent(id + "");
            list.add(student);
        }
        studentService.updateListByIdBeginEnd(list);
        return "ok";
    }

    // --------------------------------------------- 事务分割线 ---------------------------------------------
    // 有事务管理, 要么全部成功, 要么全部失败
    @GetMapping(value = "/transaction/student/insert/for")
    @Transactional
    public String insertListTransaction(String id) {
        long maxId = studentService.getMaxId() + 1;
        for (int i = 0; i < 2; i++) {
            long pId = maxId + i;
            Student student = getStudent(pId + "");
            studentService.insert(student);
        }
        if (null != id && !id.isEmpty()) {
            Student student = getStudent(id + "");
            studentService.insert(student);
        }
        return "ok";
    }

    public Student getStudent(String id) {
        String wId = "";
        if (null != id && !id.isEmpty()) {
            wId = id;
        } else {
            wId = String.valueOf(studentService.getMaxId() + 1);
        }
        Student student = new Student();
        student.setId(new BigDecimal(wId));
        student.setName(UUID.randomUUID().toString().substring(0, 3));
        student.setAge(new BigDecimal("20"));
        return student;
    }
}
