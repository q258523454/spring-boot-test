package com.trans.service.impl;

import com.BaseJunit;
import com.alibaba.fastjson.JSON;
import com.trans.entity.Student;
import com.trans.service.StudentService;
import com.trans.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

@Slf4j
public class MysqlMybatisSelectCacheTest extends BaseJunit {
    @Autowired
    private StudentService studentService;


    /**
     * Oracle的测试情况见:com.oracle.base.controller.OracleMybatisSelectCacheTest
     * 开启事务的情况下，在子事务对a数据进行修改后.
     * 外层事务仍然是查询的之前未修改的数据, 相当于可重复读 [mysql,oracle一致]
     */
    @Test
    @Transactional
    public void test1() {
        List<Student> students = studentService.selectAllByUsername("123");
        Long id = students.get(0).getId();
        // 查询结果:{"id":xx,"username":"123"}
        log.info("查询id{}，结果:{}", id, JSON.toJSONString(studentService.selectById(id)));
        // 更新后:{"id":xx,"username":"xiugai"}
//        requiredNew(id);
        requiredSer(id);
        // 查询结果:{"id":xx,"username":"123"}, 不管 sql 还是 Oracle 都一样, 走的缓存
        // 原因: 走的缓存
        log.info("查询id{}，结果:{}", id, JSON.toJSONString(studentService.selectById(id)));
        // 特别注意, 查询All的结果,是不走缓存的:
        // mysql:{"id":xx,"username":"123"}         —— mysql可重复读
        // oracle:{"id":xx,"username":"xiugai"}     —— oracle 读已提交
        log.info("查询All{}，结果:{}", id, JSON.toJSONString(studentService.selectAllStudent()));

    }

    /**
     * 外层没有事务
     * 开启事务的情况下，在子事务对a数据进行修改后.
     * 外层查询的是最新的a数据,相当于读已提交[mysql,oracle一致]
     */
    @Test
    public void test2() {
        List<Student> students = studentService.selectAllByUsername("123");
        Long id = students.get(0).getId();
        // 查询结果:{"id":xx,"username":"123"}
        log.info("查询id{}，结果:{}", id, JSON.toJSONString(studentService.selectById(id)));
        // 更新后:{"id":xx,"username":"xiugai"}
//        requiredNew(id);
        requiredSer(id);
        // 查询结果:{"id":xx,"username":"xiugai"}
        log.info("查询id{}，结果:{}", id, JSON.toJSONString(studentService.selectById(id)));
        log.info("查询All{}，结果:{}", id, JSON.toJSONString(studentService.selectAllStudent()));
    }


    /**
     * 新开事务来更新信息
     * @param id 学生id
     */
    public void requiredNew(long id) {
        PlatformTransactionManager wPlatformTransactionManager = SpringContextHolder.getBean(PlatformTransactionManager.class);
        TransactionStatus wTransactionStatus = wPlatformTransactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        try {
            Student student = studentService.selectById(id);
            log.info("更新前:{}", JSON.toJSON(student));
            student.setUsername("xiugai");
            int i = studentService.updateById(student, id);
            log.info("更新后:{}", JSON.toJSON(studentService.selectById(id)));
            wPlatformTransactionManager.commit(wTransactionStatus);
        } catch (Exception ex) {
            wPlatformTransactionManager.rollback(wTransactionStatus);
            throw ex;
        }
    }

    /**
     * 新开事务来更新信息
     * @param id 学生id
     */
    public void requiredSer(long id) {
        PlatformTransactionManager wPlatformTransactionManager = SpringContextHolder.getBean(PlatformTransactionManager.class);
        TransactionStatus wTransactionStatus = wPlatformTransactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.ISOLATION_SERIALIZABLE));
        try {
            Student student = studentService.selectById(id);
            log.info("更新前:{}", JSON.toJSON(student));
            student.setUsername("xiugai");
            int i = studentService.updateById(student, id);
            log.info("更新后:{}", JSON.toJSON(studentService.selectById(id)));
            wPlatformTransactionManager.commit(wTransactionStatus);
        } catch (Exception ex) {
            wPlatformTransactionManager.rollback(wTransactionStatus);
            throw ex;
        }
    }


}