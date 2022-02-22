package com.mysql.base.controller;

import com.BaseJunit;
import com.alibaba.fastjson.JSON;
import com.mysql.level.dao.StudentMapper;
import com.mysql.level.entity.Student;
import com.mysql.level.util.SpringContextHolder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

@Component
public class ControllerTest extends BaseJunit {
    private static final Logger logger = LoggerFactory.getLogger(ControllerTest.class);

    @Autowired
    private StudentMapper studentMapper;

    @Test
    public void selectAll() {
        logger.info("selectAll：\n" + JSON.toJSONString(studentMapper.selectAll()));
    }

    /**
     * 第1种情况: 不带事务, 默认一级缓存不生效, 因为每次都是 Creating a new SqlSession
     * {@link org.mybatis.spring.SqlSessionTemplate}
     * Spring集成的mybatis默认用的 SqlSessionTemplate 类
     * 不开启事务的情况下，每次执行mapper都是不一样的 SqlSession,
     * 因此mybatis一级缓存是无效的
     */
    @Test
    public void selectCacheInValid() {
        for (int i = 0; i < 5; i++) {
            // 每次 Creating a new SqlSession
            List<Student> students = studentMapper.selectByName("1");
            logger.info("不加事务和注解,第{}次查询结果:{}", i, JSON.toJSONString(students));
        }
    }

    /**
     * 第2种情况:开启事务(注解+手动),一级缓存有效
     * 开启事务的情况下，每次执行mapper在当前事务下都是同一个 SqlSession
     * 因此mybatis一级缓存有效: 参数相同的情况下,select直接读取缓存,不走数据库
     * 原因参考 ThreadLocal
     * {@link org.springframework.transaction.support.TransactionSynchronizationManager}
     */
    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void selectCacheValid1() {
        for (int i = 0; i < 5; i++) {
            // 每次 Fetched SqlSession, 因条件相同, 直接走 mybatis 一级缓存
            List<Student> students = studentMapper.selectByName("1");
            logger.info("注解开启事务， 第{}次查询结果:{}", i, JSON.toJSON(students));
        }
    }

    // 效果同上. 开启事务(手动):一级缓存有效
    @Test
    public void selectCacheValid2() {
        PlatformTransactionManager wPlatformTransactionManager = SpringContextHolder.getBean(PlatformTransactionManager.class);
        TransactionStatus wTransactionStatus = wPlatformTransactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        try {
            for (int i = 0; i < 5; i++) {
                List<Student> students = studentMapper.selectByName("1");
                logger.info("手动开启事务，第{}次查询结果:{}", i, JSON.toJSON(students));
            }
            wPlatformTransactionManager.commit(wTransactionStatus);
        } catch (Exception ex) {
            wPlatformTransactionManager.rollback(wTransactionStatus);
            throw ex;
        }
    }

    /**
     * 第3种情况:嵌套事务更新, 外层事务一级缓存有效
     * 这是一次生产事故:贴现通多扣钱, 解决方式:每次查询开事务(新开sqlsession),强制走数据库
     * 如果用selectAll不走缓存,(Oracle-读已提交)可查到最新数据, (Mysql-可重复读)仍然不会查到最新数据
     */
    @Test
    @Transactional
    public void nestedTransactionUpdate() {
        Student student1 = studentMapper.selectByPrimaryKey(1);
        // 第1次查询id=1:{"age":18,"id":1,"name":"1"}
        logger.info("第1次查询id=1:" + JSON.toJSONString(student1));

        Student student = studentMapper.selectByPrimaryKey(1);
        // 新开事务更新(用手动事务,注解可能不走AOP)
        // 更新后:{"age":18,"id":1,"name":"xiugai"}
        doUpdateRequiredNew(student.getId());

        Student student2 = studentMapper.selectByPrimaryKey(1);
        // 相同查询条件, 走缓存. 第2次查询id=1:{"age":18,"id":1,"name":"1"}
        logger.info("第2次查询id=1:" + JSON.toJSONString(student2));

        // selectAll 不走缓存, 但是 mysql 是可重复读: {"age":18,"id":1,"name":"1"}
        List<Student> studentAll = studentMapper.selectAll();
        logger.info("查询ALL:" + JSON.toJSONString(studentAll));
    }


    /**
     * 一级缓存失效：
     * 1.同事务下 增删改 操作
     * 2.查询条件不一致
     * 3.不同sqlsession
     * 两次查询之间有update操作, mybatis一级缓存失效.
     */
    @Test
    @Transactional
    public void doUpdate() {
        Student student1 = studentMapper.selectByPrimaryKey(1);
        // 第1次查询id=1:{"age":18,"id":1,"name":"1"}
        logger.info("第1次查询id=1:" + JSON.toJSONString(student1));

        // 更新
        Student student = studentMapper.selectByPrimaryKey(1);
        doUpdate(student.getId());

        Student student2 = studentMapper.selectByPrimaryKey(1);
        // 相同查询条件, 走缓存. 第2次查询id=1:{"age":18,"id":1,"name":"1"}
        logger.info("第2次查询id=1:" + JSON.toJSONString(student2));
    }


    /**
     * 新开事务来更新信息
     */
    public void doUpdateRequiredNew(int id) {
        PlatformTransactionManager wPlatformTransactionManager = SpringContextHolder.getBean(PlatformTransactionManager.class);
        TransactionStatus wTransactionStatus = wPlatformTransactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        try {
            // 更新
            Student student = studentMapper.selectByPrimaryKey(id);
            String olderName = student.getName();
            student.setName("xiugai");
            studentMapper.updateByName(student, olderName);
            logger.info("更新后:{}", JSON.toJSONString(student));
            wPlatformTransactionManager.commit(wTransactionStatus);
        } catch (Exception ex) {
            wPlatformTransactionManager.rollback(wTransactionStatus);
            throw ex;
        }
    }

    /**
     * 普通更新
     */
    public void doUpdate(int id) {
        Student studentUpdate = studentMapper.selectByPrimaryKey(id);
        String olderName = studentUpdate.getName();
        studentUpdate.setName("xiugai");
        studentMapper.updateByName(studentUpdate, olderName);
        logger.info("更新后:{}", JSON.toJSONString(studentUpdate));
    }


}