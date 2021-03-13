package com.oracle.batch.controller;

import com.oracle.batch.entity.Student;
import com.oracle.batch.service.IOracleStudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Author: zhangj
 * @Date: 2019-12-16
 * @Version 1.0
 */

@RestController
public class OracleControllerInsert_Batch {
    private static final Logger logger = LoggerFactory.getLogger(OracleControllerInsert_Batch.class);

    @Autowired
    private IOracleStudentService studentService;


    /**
     * --------------------------------------------------------------------------------------------------
     *                                  1000条时间     备注
     * 循环单次单条插入(无Spring事务管理)    3174 ms       多次创建SqlSession
     * 循环单次单条插入(有Spring事务管理)    1224 ms       单次创建SqlSession, Spring 好像复用了上一次SqlSession
     * 单次多条insert批量插入(BEGIN END)    117 ms       单,有无 @Transactional 对效率影响不大, 注意SQL本身具有事务性
     * 单次单条insert批量插入(foreach)      39 ms        单,有无 @Transactional 对效率影响不大, 注意SQL本身具有事务性
     * --------------------------------------------------------------------------------------------------
     */


    /**
     * 循环单次单条插入-无Spring事务管理
     * 解析: 每次执行都会创建 SqlSession, 耗时极大
     * 1000条: 3174 ms
     */
    @GetMapping(value = "/effective/student/insert/for")
    public String insertList(int num) {
        long start = System.currentTimeMillis();
        long maxId = studentService.getMaxId() + 1;
        for (int i = 0; i < num; i++) {
            long pId = maxId + i;
            Student student = getStudent(pId + "");
            studentService.insert(student);
        }
        long end = System.currentTimeMillis();
        System.out.println("循环插入-无事务 执行时间:" + (end - start));

        return "ok";
    }

    /**
     * 循环单次单条插入-有事务 Spring事务管理
     * 解析: Spring事务管理后, SqlSession只创建一次, spring 好像复用 SqlSession
     * 1000条: 1224 ms
     */
    @GetMapping(value = "/effective/student/insert/fortrans")
    @Transactional
    public String insertListTrans(int num) {
        long start = System.currentTimeMillis();
        long maxId = studentService.getMaxId() + 1;
        for (int i = 0; i < num; i++) {
            long pId = maxId + i;
            Student student = getStudent(pId + "");
            studentService.insert(student);
        }
        long end = System.currentTimeMillis();
        logger.info("循环插入-有事务 执行时间:" + (end - start));
        return "ok";
    }

    /**
     * 单次多条insert批量插入(BEGIN END) ,加不加 @Transactional 对效率影响不大
     * 解析: 只有一次 SqlSession,
     * 1000 条: 96、117
     * 10000 条: 1352、1391
     */
    // @Transactional // 基本无影响
    @GetMapping(value = "/effective/student/insert/beginend")
    public String insertListBeginEnd(int num) {
        long start = System.currentTimeMillis();
        long maxId = studentService.getMaxId() + 1;
        List<Student> studentList = new ArrayList<>();

        for (int i = 0; i < num; i++) {
            long pId = maxId + i;
            Student student = getStudent(pId + "");
            studentList.add(student);
        }
        int i = studentService.insertListBeginEnd(studentList);
        logger.info("Mybatis SQL return :" + studentService.insertListBatch(studentList));
        long end = System.currentTimeMillis();
        logger.info("单次多条insert批量插入(BEGIN END) 执行时间:" + (end - start));
        return "ok";
    }


    /**
     * ★★★推荐写法★★★
     * 单次单条insert批量插入(batch) (即使不加@Transactional, SQL本身也是具有事务性的)
     * 1000 条: 45、39
     * 10000条: 302、311、292、311
     */
    // @Transactional // 对速度基本无影响
    @GetMapping(value = "/effective/student/insert/batch")
    public String insertList2(int num) {
        long start = System.currentTimeMillis();
        List<Student> studentList = new ArrayList<>();
        long maxId = studentService.getMaxId() + 1;
        for (int i = 0; i < num; i++) {
            long pId = maxId + i;
            Student student = getStudent(pId + "");
            studentList.add(student);
        }
        logger.info("Mybatis SQL return :" + studentService.insertListBatch(studentList));
        long end = System.currentTimeMillis();
        logger.info("批量插入 执行时间:" + (end - start));
        return "ok";
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
