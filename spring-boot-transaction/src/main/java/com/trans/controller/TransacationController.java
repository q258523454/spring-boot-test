package com.trans.controller;

import com.alibaba.fastjson.JSONObject;
import com.trans.entity.Student;
import com.trans.entity.Teacher;
import com.trans.service.OtherService;
import com.trans.service.StudentService;
import com.trans.service.TeacherService;
import com.trans.service.TransactionalService;
import com.trans.util.MultiByZero;
import com.trans.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.util.Date;

/**
 * Created By
 *
 * @author :   zhangj
 * @date :   2019-02-22
 */

@Slf4j
@RestController
public class TransacationController {
    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TransactionalService transactionalService;

    @Autowired
    private OtherService otherService;

    @RequestMapping(value = "/noTransactionOrg", method = RequestMethod.GET)
    public String testTransaction() throws Exception {
        Student user1 = new Student("student_noTransacation1", "123", new Date());
        Student user2 = new Student("student_noTransacation2", "123", new Date());
        System.out.println(JSONObject.toJSONString(user1));
        System.out.println(JSONObject.toJSONString(user2));
        studentService.insertStudent(user1); // ①
        MultiByZero.multiByZero();  // 出现ArithmeticException异常, ①会插入,②不会插入
        studentService.insertStudent(user2); // ②
        return "1";
    }

    @Transactional
    @RequestMapping(value = "/transactionOrg", method = RequestMethod.GET)
    public String transactionOrg() throws Exception {
        Student student = new Student("student_transactionOrg", "123", new Date());
        Teacher teacher = new Teacher("teacher_transactionOrg", "123", new Date());
        System.out.println(JSONObject.toJSONString(student));
        System.out.println(JSONObject.toJSONString(teacher));
        studentService.insertStudent(student); // ①不会插入
        MultiByZero.multiByZero();  // 出现ArithmeticException异常, ①不会插入,②不会插入，因为有@Transactional
        teacherService.insertTeacher(teacher); // ②不会插入
        return "1";
    }

    // ------------------------- BEGIN -------------------------------
    // 默认为运行时异常回归, 检查异常,如IOException, 则无效. 如果想要回滚,需指定 rollbackFor = Exception.class
    @Transactional
    @RequestMapping(value = "/transactionOrgIOException", method = RequestMethod.GET)
    public String transactionOrgIOException() throws Exception {
        Student student = new Student("transactionOrgIOException", "123", new Date());
        Teacher teacher = new Teacher("transactionOrgIOException", "123", new Date());
        System.out.println(JSONObject.toJSONString(student));
        System.out.println(JSONObject.toJSONString(teacher));
        // 出现 FileNotFoundException 异常, @Transactional无效, ①会插入,②不会插入. 因为默认回滚 RuntimeException
        studentService.insertStudent(student); // ①会插入
        FileInputStream fileInputStream = new FileInputStream("/User/no.txt");
        teacherService.insertTeacher(teacher); // ②不会插入
        return "1";
    }
    // ------------------------- END -------------------------------


    // ------------------------- BEGIN -------------------------------
    // 同类方法自我调用,事务无效 (前提:注解声明式事务,没有走Spring代理. 编程式事务不会受该规则影响)
    // 因为Spring的注解事务是通过代理对象生效的,对象自我调用没有触发Spring代理机制
    @RequestMapping(value = "/callOwnTransMethod", method = RequestMethod.GET)
    public void callOwnTransMethod() throws Exception {
        // 如果是手动开启事务(编程式事务),调用内部方法，事务当然生效
        // eg:inner_requires_new_man()
        transactionOrg();
    }

    // 同类方法自我调用,事务无效 (前提:注解声明式事务,没有走Spring代理. 编程式事务不会受该规则影响)
    @RequestMapping(value = "/callServiceTransInterfaceByInnerMethod", method = RequestMethod.GET)
    public void callServiceTransMethodByInnerMethod() throws Exception {
        // 原理同上
        transactionalService.callOwnPublicMethod();
    }
    // ------------------------- END -------------------------------


    // ------------------------- BEGIN -------------------------------
    // 通过spring bean, 直接调用注解方法, 有效 【常用】
    @RequestMapping(value = "/callServiceTransMethod", method = RequestMethod.GET)
    public void callServiceTransMethod() throws Exception {
        transactionalService.publicMethod();
    }

    // 通过beanA, 间接调用beanB注解方法, 有效 【常用】
    // 只要是跨service开启事务调用,都是有效的, 例如下面的 beanA普通方法->beanA事务方法->beanB事务方法
    @RequestMapping(value = "/publicCallPrivateWhichCallSpringBeanTrans", method = RequestMethod.GET)
    public void publicCallPrivateWhichCallSpringBeanTrans() throws Exception {
        // 有效
        otherService.publicCallPrivateWhichCallSpringBeanTrans();
    }
    // ------------------------- END -------------------------------


    // 注解@Transactional方法上, 无论子方法是私有还是公有方法, 都有效
    @RequestMapping(value = "/callServiceTransInterface", method = RequestMethod.GET)
    public void callServiceTransMethodByInnerTransMethod() throws Exception {
        transactionalService.transInMethod();
    }


    /**
     * 编程式事务(手动事务),不走spring代理,直接生效
     */
    public void inner_requires_new_man() throws Exception {
        PlatformTransactionManager wPlatformTransactionManager = SpringContextHolder.getBean(PlatformTransactionManager.class);
        TransactionStatus wTransactionStatus = wPlatformTransactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        try {
            Student student = new Student("publicMethod_REQUIRES_NEW_SELF", "123", new Date());
            Teacher teacher = new Teacher("publicMethod_REQUIRES_NEW_SELF", "123", new Date());
            log.info(JSONObject.toJSONString(student));
            log.info(JSONObject.toJSONString(teacher));
            studentService.insertStudent(student); // ①
            // 出现ArithmeticException异常, ①不会插入,②不会插入
            MultiByZero.multiByZero();
            teacherService.insertTeacher(teacher); // ②
            log.info("提交完成");
        } catch (Exception ex) {
            log.info("error:", ex);
            log.info("开始回滚");
            wPlatformTransactionManager.rollback(wTransactionStatus);
            log.info("回滚完成");
        }
    }

}

