package com.mysql.base.controller;

import com.BaseJunit;
import com.alibaba.fastjson.JSON;
import com.mysql.base.dao.StudentMapper;
import com.mysql.base.entity.Student;
import com.mysql.base.util.SpringContextHolder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

public class ControllerTest extends BaseJunit {
    private static final Logger logger = LoggerFactory.getLogger(ControllerTest.class);

    @Autowired
    private StudentMapper studentMapper;

    @Test
    public void selectAll() {
        logger.info("selectAll：\n" + JSON.toJSONString(studentMapper.selectAll()));
    }

    /**
     * spring集成mybatis,默认使用mapper, 一级缓存是无效的
     */
    @Test
    public void test1() {
        selectCacheInValid();
    }

    /**
     * 注解开启事务,mybatis 一级缓存有效
     */
    @Test
    @Transactional
    public void test2() {
        selectCacheValid1();
    }

    /**
     * 手动开启事务,mybatis 一级缓存有效
     * ThreadLocal
     */
    @Test
    public void test3() {
        selectCacheValid2();
    }

    /**
     * {@link org.mybatis.spring.SqlSessionTemplate}
     * Spring集成的mybatis默认用的 SqlSessionTemplate 类
     * 不开启事务的情况下，每次执行mapper都是不一样的 SqlSession,
     * 因此mybatis一级缓存是无效的
     */
    public void selectCacheInValid() {
        for (int i = 0; i < 10; i++) {
            List<Student> students = studentMapper.selectByName("1");
            logger.info("不加事务和注解,第{}次查询结果:{}", i, JSON.toJSON(students));
        }
    }

    /**
     * 开启事务的情况下，每次执行mapper在当前事务下都是同一个 SqlSession
     * 因此mybatis一级缓存有效: 参数相同的情况下,select直接读取缓存,不走数据库
     * 原因参考 ThreadLocal
     * {@link org.springframework.transaction.support.TransactionSynchronizationManager}
     */
    @Transactional
    public void selectCacheValid1() {
        for (int i = 0; i < 10; i++) {
            List<Student> students = studentMapper.selectByName("1");
            logger.info("注解开启事务， 第{}次查询结果:{}", i, JSON.toJSON(students));
        }
    }

    // 效果同上
    public void selectCacheValid2() {
        PlatformTransactionManager wPlatformTransactionManager = SpringContextHolder.getBean(PlatformTransactionManager.class);
        TransactionStatus wTransactionStatus = wPlatformTransactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        try {
            for (int i = 0; i < 10; i++) {
                List<Student> students = studentMapper.selectByName("1");
                logger.info("手动开启事务，第{}次查询结果:{}", i, JSON.toJSON(students));
            }
            wPlatformTransactionManager.commit(wTransactionStatus);
        } catch (Exception ex) {
            wPlatformTransactionManager.rollback(wTransactionStatus);
            throw ex;
        }
    }

    public void doUpdate() {
        PlatformTransactionManager wPlatformTransactionManager = SpringContextHolder.getBean(PlatformTransactionManager.class);
        TransactionStatus wTransactionStatus = wPlatformTransactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        try {
            // TODO 业务操作
            Student student = studentMapper.selectByPrimaryKey(1);
            student.setAge(2);
            studentMapper.updateByName(student, student.getName());
            Student studentNew = studentMapper.selectByPrimaryKey(1);
            wPlatformTransactionManager.commit(wTransactionStatus);
        } catch (Exception ex) {
            wPlatformTransactionManager.rollback(wTransactionStatus);
            throw ex;
        }
    }
}