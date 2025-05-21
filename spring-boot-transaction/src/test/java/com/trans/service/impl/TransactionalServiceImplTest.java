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
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

@Slf4j
public class TransactionalServiceImplTest extends BaseJunit {

    @Autowired
    private StudentService studentService;

    @Test
    public void test() {
        PlatformTransactionManager wPlatformTransactionManager = SpringContextHolder.getBean(PlatformTransactionManager.class);
        TransactionStatus wTransactionStatus = wPlatformTransactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED));
        try {
            // TODO 业务操作
            List<Student> students = studentService.selectAllStudent();
            log.info("更新结果:{}", JSON.toJSON(students));
            wPlatformTransactionManager.commit(wTransactionStatus);
            log.info("提交完成");
        } catch (Exception ex) {
            log.info("开始回滚");
            wPlatformTransactionManager.rollback(wTransactionStatus);
            log.info("回滚完成");
            throw ex;
        }
    }
}