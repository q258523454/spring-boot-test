package com.oracle.base.controller;

import com.alibaba.fastjson.JSON;
import com.oracle.BaseJunit;
import com.oracle.base.entity.Student;
import com.oracle.base.service.IOracleStudentService;
import com.oracle.base.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.math.BigDecimal;
import java.util.List;


@Slf4j
public class OracleMybatisSelectCacheTest extends BaseJunit {

    @Autowired
    private IOracleStudentService studentService;


    @Test
    public void t1() {
        log.info(JSON.toJSONString(studentService.selectAll()));
    }

    /**
     * 开启事务的情况下，在子事务对a数据进行修改后.
     * 外层事务仍然是查询的之前未修改的数据(走缓存) [mysql,oracle一致], 解决办法:该查询通过新开事务查询
     */
    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void test1() {
        Student student = new Student();
        student.setName("123");
        List<Student> students = studentService.selectByName(student);
        BigDecimal id = students.get(0).getId();
        // 查询结果:{"id":xx,"username":"123"}
        log.info("查询id{}，结果:{}", id, JSON.toJSONString(studentService.selectByPrimaryKey(id)));
        // 更新后:{"id":xx,"username":"xiugai"}
        requiredNew(id);
        // 查询结果:{"id":xx,"username":"123"},
        // 原因: 走的缓存
        log.info("查询id{}，结果:{}", id, JSON.toJSONString(studentService.selectByPrimaryKey(id)));
        // 不走缓存的查询结果:
        // mysql:{"id":xx,"username":"123"}         —— mysql可重复读, 数据未更新
        // oracle:{"id":xx,"username":"xiugai"}     —— oracle读已提交, 数据已更新
        log.info("查询All{}，结果:{}", id, JSON.toJSONString(studentService.selectAll()));
    }

    /**
     * 外层没有事务
     * 开启事务的情况下，在子事务对a数据进行修改后.
     * 外层查询的是最新的数据(不走缓存)[mysql,oracle一致]
     */
    @Test
    public void test2() {
        Student student = new Student();
        student.setName("123");
        List<Student> students = studentService.selectByName(student);
        BigDecimal id = students.get(0).getId();

        // 查询结果:{"id":xx,"username":"123"}
        log.info("查询id{}，结果:{}", id, JSON.toJSONString(studentService.selectByPrimaryKey(id)));
        // 更新后:{"id":xx,"username":"xiugai"}
        requiredNew(id);
        // 查询结果:{"id":xx,"username":"xiugai"} —— 新的Session
        log.info("查询id{}，结果:{}", id, JSON.toJSONString(studentService.selectByPrimaryKey(id)));
        // 查询结果:{"id":xx,"username":"xiugai"} —— 新的Session
        log.info("查询All{}，结果:{}", id, JSON.toJSONString(studentService.selectAll()));

    }


    /**
     * 新开事务来更新信息
     * @param id 学生id
     */
    public void requiredNew(BigDecimal id) {
        PlatformTransactionManager wPlatformTransactionManager = SpringContextHolder.getBean(PlatformTransactionManager.class);
        TransactionStatus wTransactionStatus = wPlatformTransactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        try {
            Student student = studentService.selectByPrimaryKey(id);
            log.info("更新前:{}", JSON.toJSON(student));
            student.setName2("xiugai");
            int i = studentService.updateById(student, id);
            log.info("更新后:{}", JSON.toJSON(studentService.selectByPrimaryKey(id)));
            wPlatformTransactionManager.commit(wTransactionStatus);
        } catch (Exception ex) {
            wPlatformTransactionManager.rollback(wTransactionStatus);
            throw ex;
        }
    }

}