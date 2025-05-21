package com.myjetcache.controller;

import com.myjetcache.entity.Student;
import com.myjetcache.service.IOracleStudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Description
 * @date 2020-04-26 11:22
 * @modify
 */
@RestController
public class OracleControllerUpdate_Batch_AffectRows_Aop_block {
    private static final Logger logger = LoggerFactory.getLogger(OracleControllerUpdate_Batch_AffectRows_Aop_block.class);


    @Autowired
    private IOracleStudentService studentService;

    /**
     * --------------------------------------------------------------------------------------------------
     *  总结: 不管使不使用AOP的编程式, 都要注意如下场景
     *  1. 嵌套事务下 事务嵌套了另外一个事务, 默认情况下两个事务无法同一数据进行修改, 会导致死锁
     *  2. 如果不是嵌套事务, 两个平级事务不会受影响
     * --------------------------------------------------------------------------------------------------
     */


    /**
     * 不使用AOP,正常使用嵌套 REQUIRES_NEW 的情况下, 对同一数据修改会出现阻塞的情况
     * 例如: ①REQUIRES_NEW 嵌套了 {③REQUIRES_NEW(AOP)}, ②REQUIRES 实际是跟 ①同一个事务
     * 注意 ③是在①REQUIRES_NEW事务下的, 因此 ①和②是同一个事务, ②操作的数据并不会提交, 导致③操作同一数据进行等待
     * 原因:第一层 REQUIRES_NEW 事务没有提交,第二层事务等待
     * 如果把最外层的事务①去掉, 可以正常执行
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW) // ①REQUIRES_NEW
    @GetMapping(value = "/update/requirenew/requirenew/block")
    public String block() {
        Student student1 = getStudent(1 + "");
        student1.setAge(BigDecimal.valueOf(1));
        student1.setName("update11");
        studentService.updateRequire(student1); // ①REQUIRES

        Student student2 = getStudent(1 + "");
        student2.setAge(BigDecimal.valueOf(22));
        student2.setName("update22");
        studentService.updateRequireNew(student2); // ③REQUIRES_NEW
        return "ok";
    }

    /**
     * AOP切面阻塞
     * 嵌套事务,操作的数据是同一行数据,原因同上
     */
    @GetMapping(value = "/update/aop/requirenew/requirenew/block")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String requireNewRequireNewBlock() {
        List<Student> list1 = new ArrayList<>();
        Student student = getStudent(1 + "");
        student.setAge(BigDecimal.valueOf(100));
        student.setName("update");
        studentService.updateRequire(student);

        List<Student> list2 = new ArrayList<>();
        student = getStudent(1 + "");
        student.setAge(BigDecimal.valueOf(55));
        list2.add(student);
        int i = studentService.updateListByIdBatchTransAopHaveTransSelf(list2);
        logger.info("Mybatis SQL return :" + i);
        return i + "";
    }

    /**
     * AOP切面不阻塞
     * 虽然是嵌套事务,但是操作的不是同一条数据,不会引起wait commit而阻塞
     */
    @GetMapping(value = "/update/aop/requirenew/unblock1")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String RequireNewNestedBlock2() {
        List<Student> list = new ArrayList<>();
        Student student = getStudent(1 + "");
        student.setAge(BigDecimal.valueOf(88));
        student.setName("update");
        studentService.updateRequire(student);

        list = new ArrayList<>();
        student = getStudent(2 + "");
        student.setAge(BigDecimal.valueOf(44));
        list.add(student);
        int i = studentService.updateListByIdBatchTransAopHaveTransSelf(list);
        logger.info("Mybatis SQL return :" + i);
        return i + "";
    }

    /**
     * AOP切面不阻塞
     * 虽然updateRequireNew是 REQUIRE_NEW 事务,但是与AOP中的事务不是嵌套事务
     * 因此第一个事务studentService.updateRequireNew执行完后就已经commit了,因此不会阻塞后面的事务执行
     */
    @GetMapping(value = "/update/aop/requirenew/unblock2")
    public String notBlock() {
        List<Student> list = new ArrayList<>();
        Student student = getStudent(1 + "");
        student.setAge(BigDecimal.valueOf(100));
        student.setName("update");
        studentService.updateRequireNew(student);

        list = new ArrayList<>();
        student = getStudent(1 + "");
        student.setAge(BigDecimal.valueOf(77));
        list.add(student);
        int i = studentService.updateListByIdBatchTransAopHaveTransSelf(list);
        logger.info("Mybatis SQL return :" + i);
        return i + "";
    }

    public Student getStudent(String id) {
        String wId = "";
        if (null != id && !id.isEmpty()) {
            wId = id;
        } else {
            wId = String.valueOf(studentService.getMaxId() + 1);
        }
        Student student = new Student();
        student.setId(new BigDecimal(wId));
        student.setName(UUID.randomUUID().toString().substring(0, 3));
        student.setAge(new BigDecimal("20"));
        return student;
    }

}
