package com.trans.controller;

import com.alibaba.fastjson.JSONObject;
import com.trans.entity.Student;
import com.trans.entity.Teacher;
import com.trans.service.OtherService;
import com.trans.service.StudentService;
import com.trans.service.TeacherService;
import com.trans.service.TransactionalService;
import com.trans.util.MultiByZero;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;

/**
 * Created By
 *
 * @author :   zhangj
 * @date :   2019-02-22
 */

@RestController
public class TransacationTryCatchController {

    private Logger log = LoggerFactory.getLogger(TransacationTryCatchController.class);

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TransactionalService transactionalService;

    @Autowired
    private OtherService otherService;


    // 无效, @Transactional注解实际上就是一个拦截器, 没有抛出运行时异常, 无法回滚事务
    @Transactional
    @RequestMapping(value = "/transactionTryCatch", method = RequestMethod.GET)
    public String transactionTryCatch() {
        try {
            Student student = new Student("student_transactionOrg", "123", new Date());
            Teacher teacher = new Teacher("teacher_transactionOrg", "123", new Date());
            System.out.println(JSONObject.toJSONString(student));
            System.out.println(JSONObject.toJSONString(teacher));
            studentService.insertStudent(student); // ①
            MultiByZero.multiByZero();  // 出现ArithmeticException异常, ①不会插入,②不会插入，因为有@Transactional
            teacherService.insertTeacher(teacher); // ②
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
        return "1";
    }


    // 有效, 虽然没有抛出检查异常, 但是用 TransactionAspectSupport 手动回滚了事务
    @Transactional
    @RequestMapping(value = "/transactionTryCatchThrowCheckExceptionA", method = RequestMethod.GET)
    public String transactionTryCatchThrowCheckExceptionA() throws FileNotFoundException {
        try {
            Student student = new Student("student_transactionOrg", "123", new Date());
            Teacher teacher = new Teacher("teacher_transactionOrg", "123", new Date());
            System.out.println(JSONObject.toJSONString(student));
            System.out.println(JSONObject.toJSONString(teacher));
            studentService.insertStudent(student); // ①
            FileInputStream fileInputStream = new FileInputStream("/Mac/no.txt");
            teacherService.insertTeacher(teacher); // ②
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return "1";
    }

    // 无效, 虽然抛出了异常, 但抛出的异常是检查时异常, 默认回顾的异常是运行时异常.
    @Transactional
    @RequestMapping(value = "/transactionTryCatchThrowCheckException", method = RequestMethod.GET)
    public String transactionTryCatchThrowCheckException() throws FileNotFoundException {
        try {
            Student student = new Student("student_transactionOrg", "123", new Date());
            Teacher teacher = new Teacher("teacher_transactionOrg", "123", new Date());
            System.out.println(JSONObject.toJSONString(student));
            System.out.println(JSONObject.toJSONString(teacher));
            studentService.insertStudent(student); // ①
            FileInputStream fileInputStream = new FileInputStream("/Mac/no.txt");
            teacherService.insertTeacher(teacher); // ②
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return "1";
    }

    // 有效, 更改rollbackFor的级别, 抛出的所有异常都会回滚
    @Transactional(rollbackFor = Exception.class)
    @RequestMapping(value = "/transactionTryCatchThrowCheckException2", method = RequestMethod.GET)
    public String transactionTryCatchThrowCheckException2() throws FileNotFoundException {
        try {
            Student student = new Student("student_transactionOrg", "123", new Date());
            Teacher teacher = new Teacher("teacher_transactionOrg", "123", new Date());
            System.out.println(JSONObject.toJSONString(student));
            System.out.println(JSONObject.toJSONString(teacher));
            studentService.insertStudent(student); // ①
            FileInputStream fileInputStream = new FileInputStream("/Mac/no.txt");
            teacherService.insertTeacher(teacher); // ②
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return "1";
    }
}
