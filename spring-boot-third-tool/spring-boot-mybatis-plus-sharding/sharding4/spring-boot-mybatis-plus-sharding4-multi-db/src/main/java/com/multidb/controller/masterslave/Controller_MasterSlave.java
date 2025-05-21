package com.multidb.controller.masterslave;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.multidb.controller.StudentUtil;
import com.multidb.entity.StudentMasterSlave;
import com.multidb.service.masterslave.StudentMasterSlaveService;

import lombok.extern.slf4j.Slf4j;

import org.apache.shardingsphere.api.hint.HintManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
public class Controller_MasterSlave {

    @Autowired
    private StudentMasterSlaveService studentMasterSlaveService;

    /**
     * 增删改-主库(写库)
     */
    @GetMapping(value = "/masterslave/student/insert")
    @Transactional(value = "myMasterSlaveTransactionManager", rollbackFor = Exception.class)
    public String test1() {
        StudentMasterSlave student = StudentUtil.getRandomStudentMasterSlavetList(1).get(0);
        studentMasterSlaveService.getBaseMapper().insert(student);
        return JSON.toJSONString(student);
    }

    /**
     * 查询-备库(读库)
     */
    @GetMapping(value = "/masterslave/student/select/{id}")
    @Transactional(value = "myMasterSlaveTransactionManager", rollbackFor = Exception.class)
    public String test2(@PathVariable("id") Long id) {
        LambdaQueryWrapper<StudentMasterSlave> studentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<StudentMasterSlave> studentList = studentMasterSlaveService.getBaseMapper()
                .selectList(studentLambdaQueryWrapper.eq(StudentMasterSlave::getId, id));
        return JSON.toJSONString(studentList);
    }

    /**
     * 查询全部-备库(读库)
     */
    @GetMapping(value = "/masterslave/student/select/all")
    @Transactional(value = "myMasterSlaveTransactionManager", rollbackFor = Exception.class)
    public String test2() {
        List<StudentMasterSlave> studentList = studentMasterSlaveService.getBaseMapper().selectList(null);
        return JSON.toJSONString(studentList);
    }

    /**
     * 查询-强制主库(写库)
     */
    @GetMapping(value = "/masterslave/student/select/master/{id}")
    @Transactional(value = "myMasterSlaveTransactionManager", rollbackFor = Exception.class)
    public String test4(@PathVariable("id") Long id) {
        // 强制主库
        HintManager.getInstance().setMasterRouteOnly();
        LambdaQueryWrapper<StudentMasterSlave> studentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<StudentMasterSlave> studentList = studentMasterSlaveService.getBaseMapper()
                .selectList(studentLambdaQueryWrapper.eq(StudentMasterSlave::getId, id));
        return JSON.toJSONString(studentList);
    }

    /**
     * 查询全部-强制主库(写库)
     */
    @GetMapping(value = "/masterslave/student/select/master/all")
    @Transactional(value = "myMasterSlaveTransactionManager", rollbackFor = Exception.class)
    public String test5() {
        // 强制主库
        HintManager.getInstance().setMasterRouteOnly();
        List<StudentMasterSlave> studentList = studentMasterSlaveService.getBaseMapper().selectList(null);
        return JSON.toJSONString(studentList);
    }
}
