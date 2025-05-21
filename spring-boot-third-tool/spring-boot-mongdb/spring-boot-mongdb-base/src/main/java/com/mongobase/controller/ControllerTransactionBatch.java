package com.mongobase.controller;

import com.google.common.collect.Lists;
import com.mongobase.pojo.entity.Student;
import com.mongobase.service.StudentService;
import com.mongobase.service.StudentServiceTransaction;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
public class ControllerTransactionBatch {

    @Autowired
    private StudentService studentService;


    @Autowired
    private StudentServiceTransaction studentServiceTransaction;

    /**
     * 注意 4.0 的版本 事务下的操作不能超过 16M
     * 解决办法:
     * 1.升级mongodb
     * 2.减少事务数据量
     */
    @GetMapping("/insert/list/trans")
    @Transactional
    public String insertListTrans(int num) {
        // 测试的数据是4.0版本,复制集,不能再事务下查询, 否则会报错: Read preference in a transaction must be primary
        Long maxSid = studentService.getMaxSidNoTrans();
        List<Student> randomStudentList = Student.getRandomStudentList(num * 10, maxSid);
        List<List<Student>> partition = Lists.partition(randomStudentList, num);
        int i = 1;
        for (List<Student> studentList : partition) {
            long start = System.currentTimeMillis();
            // 带事务批量插入
            studentService.insertAll(randomStudentList);
            long end = System.currentTimeMillis();
            log.info("批量插入(带事务),第{}次,数据量{},执行时间:{}", i, studentList.size(), (end - start));
        }
        // throw new RuntimeException("测试回滚");
        return "ok";
    }

    @GetMapping("/insert/list/no/trans")
    public String insertListNoTrans(int num) {
        Long maxSid = studentService.getMaxSid();
        List<Student> randomStudentList = Student.getRandomStudentList(num * 10, maxSid);
        List<List<Student>> partition = Lists.partition(randomStudentList, num);
        int i = 1;
        for (List<Student> studentList : partition) {
            long start = System.currentTimeMillis();
            // 带事务批量插入
            studentService.insertAll(randomStudentList);
            long end = System.currentTimeMillis();
            log.info("批量插入(不带事务),第{}次,数据量{},执行时间:{}", i, studentList.size(), (end - start));
        }

        return "ok";
    }

}
