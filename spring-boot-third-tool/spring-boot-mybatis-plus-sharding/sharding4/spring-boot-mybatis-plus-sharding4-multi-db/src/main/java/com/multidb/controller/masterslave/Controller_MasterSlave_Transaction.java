package com.multidb.controller.masterslave;


import com.alibaba.fastjson.JSON;
import com.multidb.controller.StudentUtil;
import com.multidb.entity.StudentMasterSlave;
import com.multidb.service.masterslave.StudentMasterSlaveService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
public class Controller_MasterSlave_Transaction {

    @Autowired
    private StudentMasterSlaveService studentMasterSlaveService;

    /**
     * xml批量插入-主库
     */
    @GetMapping(value = "/masterslave/student/insert/batch")
    @Transactional(value = "myMasterSlaveTransactionManager", rollbackFor = Exception.class)
    public String test1() {
        List<StudentMasterSlave> studentList = StudentUtil.getRandomStudentMasterSlavetList(5);
        studentMasterSlaveService.saveBatch(studentList);
        return JSON.toJSONString("ok");
    }

    /**
     * xml批量插入-主库(回滚)
     */
    @GetMapping(value = "/masterslave/student/insert/batch/error")
    @Transactional(value = "myMasterSlaveTransactionManager", rollbackFor = Exception.class)
    public String test2() {
        List<StudentMasterSlave> studentList = StudentUtil.getRandomStudentMasterSlavetList(5);
        studentMasterSlaveService.saveBatch(studentList);
        throw new RuntimeException("error");
    }

    /**
     * xml foreach 批量插入-主库
     */
    @GetMapping(value = "/masterslave/student/xml/insert/list")
    @Transactional(value = "myMasterSlaveTransactionManager", rollbackFor = Exception.class)
    public String test11() {
        List<StudentMasterSlave> studentList = StudentUtil.getRandomStudentMasterSlavetList(5);
        int i = studentMasterSlaveService.insertList(studentList);
        return JSON.toJSONString("ok");
    }

    /**
     * xml foreach 批量插入-主库(回滚)
     */
    @GetMapping(value = "/masterslave/student/insert/xml/list/error")
    @Transactional(value = "myMasterSlaveTransactionManager", rollbackFor = Exception.class)
    public String test22() {
        List<StudentMasterSlave> studentList = StudentUtil.getRandomStudentMasterSlavetList(5);
        studentMasterSlaveService.insertList(studentList);
        throw new RuntimeException("error");
    }
}
