package com.mongobase.controller;

import com.mongobase.pojo.entity.Student;
import com.mongobase.service.StudentService;
import com.mongobase.service.StudentServiceTransaction;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
public class ControllerTransaction {

    @Autowired
    private StudentService studentService;


    @Autowired
    private StudentServiceTransaction studentServiceTransaction;

    /**
     * 注意:
     * 1.版本4.0 事务只支持副本集(复制集)
     *   版本4.2 才支持分片事务
     * 2.Spring要 5.1.1.RELEASE 以上
     * 3.SpringDataMongoDB要 2.1.1.RELEASE 以上
     *
     * 下面测试通过:
     * mongodb 4.0 复制集
     */
    @GetMapping("/insert/trans")
    public String insertTrans() {
        Long maxSid = studentService.getMaxSid();

        List<Student> list1 = new ArrayList<>();
        list1.add(Student.createStudent(maxSid + 1, "张三"));
        list1.add(Student.createStudent(maxSid + 2, "李四"));
        list1.add(Student.createStudent(maxSid + 3, "王五"));

        List<Student> list2 = new ArrayList<>();
        list2.add(Student.createStudent(maxSid + 4, "张三"));
        list2.add(Student.createStudent(maxSid + 5, "李四"));
        list2.add(Student.createStudent(maxSid + 6, "王五"));

        studentServiceTransaction.insertAllTrans(list1, list2);
        return "ok";
    }


    @GetMapping("/insert/no/trans")
    public String insertNoTrans() {
        Long maxSid = studentService.getMaxSid();
        List<Student> list1 = new ArrayList<>();
        list1.add(Student.createStudent(maxSid + 1, "张三"));
        list1.add(Student.createStudent(maxSid + 2, "李四"));
        list1.add(Student.createStudent(maxSid + 3, "王五"));

        List<Student> list2 = new ArrayList<>();
        list2.add(Student.createStudent(maxSid + 4, "张三"));
        list2.add(Student.createStudent(maxSid + 5, "李四"));
        list2.add(Student.createStudent(maxSid + 6, "王五"));

        studentServiceTransaction.insertAllNoTrans(list1, list2);
        return "ok";
    }
}
