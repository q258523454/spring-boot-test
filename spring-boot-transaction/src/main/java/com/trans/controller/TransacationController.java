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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.util.Date;


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

    /*
     * 无效场景总结:
     *
     * 1.private/protected 以及 static,final
     *   无法通过动态代理, 事务无效
     *   解决: 改成 public
     *
     * 2.未指定捕获异常
     *   有事务方法, 默认RuntimeException, 抛IOException,不回滚
     *   解决: 指定回滚Exception 或者 抛出支持回滚的异常
     *
     * 3.同类方法自我调用(this)
     *   同类方法自我调用, 被调方法的事务无效, 但其他走AOP的事务不受影响
     *   例如:
     *   beanA 有事务方法a() ——> beanA 有事务方法b() : 仅b()方法中事务无效
     *   beanA 有事务方法a() ——> beanB 有事务或无事务方法b() ——> beanB 有事务方法c() : 仅c()方法中事务无效
     *   解决: 1:将方法拆分到不同的service; 2.使用代理类调用自己; 3.自己注入自己,再通过bean调用;
     *
     * 4.catch异常, 没有抛出
     *   异常被吞掉
     *
     * 5.方法使用了 Propagation.NOT_SUPPORTED
     *   注意: 如果子方法的Propagation 指定为 NOT_SUPPORTED, 即使外层有包事务, 该方法也不会回滚.
     *
     * 6.多线程使用事务
     *   不同线程使用的不同数据库链接，不在同一个事务下
     */


    // -------------------------------- 普通场景 --------------------------------

    /**
     * 无事务,不回滚
     */
    @RequestMapping(value = "/noTransactionOrg", method = RequestMethod.GET)
    public String testTransaction() throws Exception {
        Student student = Student.createStudent("student_noTransacation1");
        Teacher teacher = Teacher.createTeacher("student_noTransacation2");
        System.out.println(JSONObject.toJSONString(student));
        System.out.println(JSONObject.toJSONString(teacher));
        studentService.insertStudent(student); // ① 插入(无事务,不回滚)
        MultiByZero.multiByZero();  // 异常
        teacherService.insertTeacher(teacher); // ② 不会插入(未执行)
        return "1";
    }

    /**
     * 有事务,回滚
     */
    @Transactional
    @RequestMapping(value = "/transactionOrg", method = RequestMethod.GET)
    public String transactionOrg() throws Exception {
        Student student = Student.createStudent("student_transactionOrg");
        Teacher teacher = Teacher.createTeacher("teacher_transactionOrg");
        System.out.println(JSONObject.toJSONString(student));
        System.out.println(JSONObject.toJSONString(teacher));
        studentService.insertStudent(student); // ① 不会插入(回滚)
        MultiByZero.multiByZero();  // 异常
        teacherService.insertTeacher(teacher); // ② 不会插入(未执行)
        return "1";
    }

    /**
     * 有事务,抛IOException,不回滚
     * 默认为运行时异常回归, 检查异常,如IOException, 则无效.
     * 如果想要回滚,需指定 rollbackFor = Exception.class
     */
    @Transactional
    @RequestMapping(value = "/transactionOrgIOException", method = RequestMethod.GET)
    public String transactionOrgIOException() throws Exception {
        Student student = Student.createStudent("transactionOrgIOException");
        Teacher teacher = Teacher.createTeacher("transactionOrgIOException");
        System.out.println(JSONObject.toJSONString(student));
        System.out.println(JSONObject.toJSONString(teacher));
        studentService.insertStudent(student); // ① 会插入 (因为IO异常不是RuntimeException,事务不会回滚)
        FileInputStream fileInputStream = new FileInputStream("/User/no.txt");
        teacherService.insertTeacher(teacher); // ② 不会插入(未执行)
        return "1";
    }


    /**
     * 有事务,private/protected 方法事务无效
     * 原因: private / protected 无法触发 CglibProxy 或 JDKProxy
     * 补充: static、final 属于类属性, 不属于代理对象, 也无法被重写, 因此无法通过动态代理, 事务无效
     */
    @Transactional
    @RequestMapping(value = "/transactionProtected", method = RequestMethod.GET)
    protected String transactionProtected() throws Exception {
        Student student = Student.createStudent("student_transactionOrg");
        Teacher teacher = Teacher.createTeacher("teacher_transactionOrg");
        System.out.println(JSONObject.toJSONString(student));
        System.out.println(JSONObject.toJSONString(teacher));
        studentService.insertStudent(student); // ① 不会插入(回滚)
        MultiByZero.multiByZero();  // 异常
        teacherService.insertTeacher(teacher); // ② 不会插入(未执行)
        return "1";
    }
    // -------------------------------- END --------------------------------


    // -------------------------------- 同类方法自我调用,事务无效 BEGIN --------------------------------

    /**
     * 同类方法自我调用,事务无效
     * 类型: beanA 无事务方法 ——> beanA 有事务方法 (无效)
     * 原因:没有走AOP代理, 解决:强制使用编程式事务(手动事务)
     */
    @RequestMapping(value = "/callOwnTransMethod1", method = RequestMethod.GET)
    public void callOwnTransMethod() throws Exception {
        transactionOrg();
        // 如果是手动开启事务(编程式事务),调用内部方法，事务当然生效
        // eg:inner_requires_new_man()
    }


    /**
     * 同类方法自我调用,事务无效
     * 类型: beanB 无事务方法 ——> beanB 有事务方法 (无效)
     */
    @RequestMapping(value = "/callOwnPublicMethod2", method = RequestMethod.GET)
    public void callServiceTransMethodByInnerMethod() {
        transactionalService.callOwnPublicMethod();
    }


    /**
     * 同类方法自我调用,事务无效
     * 类型: beanA 有事务方法 ——> beanA 有事务方法 (仅这个事务无效)
     * 特别注意：Controller 当前方法的事务是生效的, 没生效的是指没有被AOP代理的事务
     */
    @RequestMapping(value = "/callOwnPublicMethod3", method = RequestMethod.GET)
    @Transactional(propagation = Propagation.REQUIRED)
    public void callOwnTransMethod2() throws Exception {
        Student student = Student.createStudent("外层事务");
        studentService.insertStudent(student); // 回滚
        /*
         * 同类方法自我调用, 下面的 REQUIRES_NEW 无效，导致一起回滚
         * 正常情况下 外层REQUIRED出现异常，不会影响内层 REQUIRES_NEW(不回滚)
         * 如果改成 studentService.publicMethod_REQUIRES_NEW_NO_ERROR(); 则事务生效, REQUIRES_NEW不回滚, 因为走了AOP代理
         */
        innerTransactionRequiresNew();
        // transactionalService.publicMethod_REQUIRES_NEW_NO_ERROR(); // 走AOP,有效,REQUIRES_NEW 不回滚
        MultiByZero.multiByZero();  // 异常
    }


    /**
     * 同类方法自我调用,事务无效
     * 类型: beanA 有事务方法 ——> beanB 有事务/无事务 ——> beanB 有事务方法 (仅这个事务无效)
     * 特别注意: 没生效的事务仅仅是没有被AOP代理的事务, 其他事务不受影响
     */
    @RequestMapping(value = "/callOwnPublicMethod4", method = RequestMethod.GET)
    @Transactional(propagation = Propagation.REQUIRED)
    public void callOwnRequiresNewMethod4() throws Exception {
        Student student = Student.createStudent("外层事务");
        studentService.insertStudent(student);
        transactionalService.callOwn_REQUIRES_NEW_NO_ERROR(); // REQUIRES_NEW 会回滚(正常生效情况下是不会回滚的)
        MultiByZero.multiByZero();  // 异常
    }

    // ------------------------------------------------ END -----------------------------------------------------------


    // 直接调用 beanA方法(带事务), 有效 【常用】
    @RequestMapping(value = "/callServiceTransMethod", method = RequestMethod.GET)
    public void callServiceTransMethod() throws Exception {
        transactionalService.publicMethod();
    }

    /**
     * 只要是跨service开启事务调用,都是有效的
     * 例如: beanA 方法 ——> beanB 方法——> beanC 有事务方法(有效)
     */
    @RequestMapping(value = "/publicCallPrivateWhichCallSpringBeanTrans", method = RequestMethod.GET)
    public void publicCallPrivateWhichCallSpringBeanTrans() throws Exception {
        // 有效
        otherService.publicCallPrivateWhichCallSpringBeanTrans();
    }


    /**
     * 注解@Transactional方法上, 无论子方法是私有还是公有方法, 都有效
     * 注意: 内部调用的时候, 被调方法上的事务是无效的. 事务共用的是第一个方法上的事务
     */
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

    /**
     * 内部调用 - REQUIRES_NEW
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void innerTransactionRequiresNew() throws Exception {
        Student student = Student.createStudent("student_transactionOrg");
        System.out.println(JSONObject.toJSONString(student));
        studentService.insertStudent(student);
    }
}

