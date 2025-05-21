package com.trans.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.trans.dao.StudentMapper;
import com.trans.dao.TeacherMapper;
import com.trans.entity.Student;
import com.trans.entity.Teacher;
import com.trans.service.TransactionalService;
import com.trans.util.MultiByZero;
import com.trans.util.SpringContextHolder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Date;



@Service
public class TransactionalServiceImpl implements TransactionalService {

    private Logger logger = LoggerFactory.getLogger(TransactionalService.class);


    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private TeacherMapper teacherMapper;


    @Override
    public void callOwn_REQUIRES_NEW_NO_ERROR() {
        publicMethod_REQUIRES_NEW_NO_ERROR();
    }

    /**
     * 在公有方法上事务注解, 再通过接口方法调用, 无效
     * 注意:私有方法上事务注解本身就无效(无需测试)
     */
    @Override
    public void callOwnPublicMethod() {
        publicMethod(); // 无效
    }


    /**
     * 在接口方法上加事务, 无论调用私有还是公有方法, 都有效
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void transInMethod() {
        // 有效, 相当于共用一套事务
        privateMethod(); //
        // publicMethod(); // 内部调用, publicMethod()上的事务是无效的
    }


    private void privateMethod() {
        Student student = new Student("student_privateMethod", "123", new Date());
        Teacher teacher = new Teacher("teacher_privateMethod", "123", new Date());
        logger.info(JSONObject.toJSONString(student));
        logger.info(JSONObject.toJSONString(teacher));
        studentMapper.insertStudent(student); // ①
        MultiByZero.multiByZero();  // 出现ArithmeticException异常, ①不会插入,②不会插入，因为有@Transactional
        teacherMapper.insertTeacher(teacher); // ②
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void publicMethod() {
        Student student = new Student("student_publicMethod", "123", new Date());
        Teacher teacher = new Teacher("teacher_publicMethod", "123", new Date());
        logger.info(JSONObject.toJSONString(student));
        logger.info(JSONObject.toJSONString(teacher));
        studentMapper.insertStudent(student); // ① 不会插入(回滚)
        MultiByZero.multiByZero();  // 异常
        teacherMapper.insertTeacher(teacher); // ② 不会插入(未执行)
    }


    // ------------------------------------ propagation test ------------------------------------
    @Override
    public void publicMethod_NO_TRANSCATIONAL() {
        Student student = new Student("publicMethod_NO_TRANSCATIONAL", "123", new Date());
        Teacher teacher = new Teacher("publicMethod_NO_TRANSCATIONAL", "123", new Date());
        logger.info(JSONObject.toJSONString(student));
        logger.info(JSONObject.toJSONString(teacher));
        studentMapper.insertStudent(student); // ①
        MultiByZero.multiByZero();
        teacherMapper.insertTeacher(teacher); // ②
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void publicMethod_REQUIRED() throws Exception {
        Student student = new Student("publicMethod_REQUIRED", "123", new Date());
        Teacher teacher = new Teacher("publicMethod_REQUIRED", "123", new Date());
        logger.info(JSONObject.toJSONString(student));
        logger.info(JSONObject.toJSONString(teacher));
        studentMapper.insertStudent(student); // ① 回滚
        MultiByZero.multiByZero();
        teacherMapper.insertTeacher(teacher); // ② 不执行
    }

    // Propagation.NOT_SUPPORTED: 以非事务方式执行，并挂起当前事务, 已经执行了的内部方法不会随外部事物回滚
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void publicMethod_REQUIRED_NO_ERROR() {
        Student student = new Student("publicMethod_REQUIRED_NO_ERROR", "123", new Date());
        Teacher teacher = new Teacher("publicMethod_REQUIRED_NO_ERROR", "123", new Date());
        logger.info(JSONObject.toJSONString(student));
        logger.info(JSONObject.toJSONString(teacher));
        studentMapper.insertStudent(student); // ① 回滚
        teacherMapper.insertTeacher(teacher); // ② 不执行
    }


    // Propagation.NOT_SUPPORTED: 以非事务方式执行，并挂起当前事务, 已经执行了的内部方法不会随外部事物回滚
    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = false)
    public void publicMethod_NOT_SUPPORTED() throws Exception {
        Student student = new Student("publicMethod_NOT_SUPPORTED", "123", new Date());
        Teacher teacher = new Teacher("publicMethod_NOT_SUPPORTED", "123", new Date());
        logger.info(JSONObject.toJSONString(student));
        logger.info(JSONObject.toJSONString(teacher));
        studentMapper.insertStudent(student); // ①会插入
        // 出现ArithmeticException异常, ①会插入,②不会插入，因为是非事务执行, 不会回滚, 且不随外部事务回滚
        MultiByZero.multiByZero();
        teacherMapper.insertTeacher(teacher); // ②不会插入
    }

    @Override
    public void publicMethod_NOT_SUPPORTED_MAN() throws Exception {
        PlatformTransactionManager wPlatformTransactionManager = SpringContextHolder.getBean(PlatformTransactionManager.class);
        TransactionStatus wTransactionStatus = wPlatformTransactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_NOT_SUPPORTED));
        try {
            Student student = new Student("publicMethod_NOT_SUPPORTED_SELF", "123", new Date());
            Teacher teacher = new Teacher("publicMethod_NOT_SUPPORTED_SELF", "123", new Date());
            logger.info(JSONObject.toJSONString(student));
            logger.info(JSONObject.toJSONString(teacher));
            studentMapper.insertStudent(student); // ①
            // 出现ArithmeticException异常, ①会插入,②不会插入，因为是非事务执行, 不会回滚, 且不随外部事务回滚
            MultiByZero.multiByZero();
            teacherMapper.insertTeacher(teacher); // ②
            logger.info("提交完成");
        } catch (Exception ex) {
            logger.info("error:", ex);
            logger.info("开始回滚");
            wPlatformTransactionManager.rollback(wTransactionStatus);
            logger.info("回滚完成");
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void publicMethod_REQUIRES_NEW() throws Exception {
        Student student = new Student("publicMethod_REQUIRES_NEW", "123", new Date());
        Teacher teacher = new Teacher("publicMethod_REQUIRES_NEW", "123", new Date());
        logger.info(JSONObject.toJSONString(student));
        logger.info(JSONObject.toJSONString(teacher));
        studentMapper.insertStudent(student); // ①
        MultiByZero.multiByZero();  // 出现ArithmeticException异常, ①不会插入,②不会插入，因为有@Transactional
        teacherMapper.insertTeacher(teacher); // ②
    }

    @Override
    public void publicMethod_REQUIRES_NEW_MAN() throws Exception {
        PlatformTransactionManager wPlatformTransactionManager = SpringContextHolder.getBean(PlatformTransactionManager.class);
        TransactionStatus wTransactionStatus = wPlatformTransactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        try {
            Student student = new Student("publicMethod_REQUIRES_NEW_SELF", "123", new Date());
            Teacher teacher = new Teacher("publicMethod_REQUIRES_NEW_SELF", "123", new Date());
            logger.info(JSONObject.toJSONString(student));
            logger.info(JSONObject.toJSONString(teacher));
            studentMapper.insertStudent(student); // ①
            // 出现ArithmeticException异常, ①不会插入,②不会插入
            MultiByZero.multiByZero();
            teacherMapper.insertTeacher(teacher); // ②
            logger.info("提交完成");
        } catch (Exception ex) {
            logger.info("error:", ex);
            logger.info("开始回滚");
            wPlatformTransactionManager.rollback(wTransactionStatus);
            logger.info("回滚完成");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void publicMethod_REQUIRES_NEW_NO_ERROR() {
        Student student = new Student("publicMethod_REQUIRES_NEW_NO_ERROR", "123", new Date());
        Teacher teacher = new Teacher("publicMethod_REQUIRES_NEW_NO_ERROR", "123", new Date());
        logger.info(JSONObject.toJSONString(student));
        logger.info(JSONObject.toJSONString(teacher));
        studentMapper.insertStudent(student); // ①
        teacherMapper.insertTeacher(teacher); // ②
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, readOnly = false)
    public void publicMethod_NESTED() {
        Student student = new Student("publicMethod_NESTED", "123", new Date());
        Teacher teacher = new Teacher("publicMethod_NESTED", "123", new Date());
        logger.info(JSONObject.toJSONString(student));
        logger.info(JSONObject.toJSONString(teacher));
        studentMapper.insertStudent(student); // ① 回滚
        MultiByZero.multiByZero();
        teacherMapper.insertTeacher(teacher); // ② 不执行
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, readOnly = false)
    public void publicMethod_NESTED_NO_ERROR() {
        Student student = new Student("publicMethod_NESTED_NO_ERROR", "123", new Date());
        Teacher teacher = new Teacher("publicMethod_NESTED_NO_ERROR", "123", new Date());
        logger.info(JSONObject.toJSONString(student));
        logger.info(JSONObject.toJSONString(teacher));
        studentMapper.insertStudent(student); // ①
        teacherMapper.insertTeacher(teacher); // ②
    }


    @Override
    public void publicMethod_NESTED_MAN() {
        PlatformTransactionManager wPlatformTransactionManager = SpringContextHolder.getBean(PlatformTransactionManager.class);
        TransactionStatus wTransactionStatus = wPlatformTransactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_NESTED));
        try {
            Student student = new Student("publicMethod_NESTED_SELF", "123", new Date());
            Teacher teacher = new Teacher("publicMethod_NESTED_SELF", "123", new Date());
            logger.info(JSONObject.toJSONString(student));
            logger.info(JSONObject.toJSONString(teacher));
            studentMapper.insertStudent(student);
            MultiByZero.multiByZero();
            teacherMapper.insertTeacher(teacher);
            wPlatformTransactionManager.commit(wTransactionStatus);
            logger.info("提交完成");
        } catch (Exception ex) {
            logger.info("开始回滚");
            wPlatformTransactionManager.rollback(wTransactionStatus);
            logger.info("回滚完成");
        }
    }

    @Override
    public void publicMethod_NESTED_NO_ERROR_MAN() {
        PlatformTransactionManager wPlatformTransactionManager = SpringContextHolder.getBean(PlatformTransactionManager.class);
        TransactionStatus wTransactionStatus = wPlatformTransactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_NESTED));
        try {
            Student student = new Student("publicMethod_NESTED_NO_ERROR_SELF", "123", new Date());
            Teacher teacher = new Teacher("publicMethod_NESTED_NO_ERROR_SELF", "123", new Date());
            logger.info(JSONObject.toJSONString(student));
            logger.info(JSONObject.toJSONString(teacher));
            studentMapper.insertStudent(student); // ①
            teacherMapper.insertTeacher(teacher); // ②
            wPlatformTransactionManager.commit(wTransactionStatus);
            logger.info("提交完成");
        } catch (Exception ex) {
            logger.info("开始回滚");
            wPlatformTransactionManager.rollback(wTransactionStatus);
            logger.info("回滚完成");
        }
    }
}
