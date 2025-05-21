package com.myjetcache.controller;

import com.myjetcache.entity.Student;
import com.myjetcache.service.IOracleStudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class OracleControllerUpdate_Batch_AffectRows_Aop {
    private static final Logger logger = LoggerFactory.getLogger(OracleControllerUpdate_Batch_AffectRows_Aop.class);

    @Autowired
    private IOracleStudentService studentService;

    /**
     * --------------------------------------------------------------------------------------------------
     *  由于Oracle更新的条数限制以及行内规范,不允许一次性更新超过1000条数据.
     *  需要对Update做切面, 将update list 分成一个一个的sublist, 因此需要对update的代码做分批处理.
     *  总结:
     *  AOP使用事务的话只能使用编程式事务, 注解事务是通过代理生效的, 自我调用没有触发Spring代理机制
     *  AOP-Batch切面:
     *
     *  Service无事务  AOP无事务          已执行的Subbatch不回滚(SQL本身就具有事务特性)
     *  Service无事务  AOP有事务          切面出现异常且未捕获处理,已执行的"切片"全部回滚
     *
     *  Service有事务  AOP无事务          异常不处理,触发事务回滚, service和切面全部回滚 (默认REQUIRE级别)
     *  Service有事务  AOP无事务          Service捕获切面的异常, 相当于事务不生效, AOP已执行的"切片"不会回滚.
     * --------------------------------------------------------------------------------------------------
     */

    /**
     * 简单测试:batch SQL本身就具有事务特性
     */
    @GetMapping(value = "/update/batch/org")
    public String batch(int num) {
        List<Student> list = new ArrayList<>();
        List<Student> selectAll = studentService.selectAll();
        // 对数据库的前num个数进行修改,注意数据库必须有数据,切面默认切分list的sublist大小为5
        for (int i = 0; i < num; i++) {
            BigDecimal id = selectAll.get(i).getId();
            Student student = getStudent(id + "");
            student.setAge(BigDecimal.valueOf(num));
            list.add(student);
        }
        int i = studentService.updateListByIdBatchOrg(list);
        logger.info("Mybatis SQL return :" + i);
        return i + "";
    }

    /**
     * Service无事务, AOP无事务【正在执行切片SubBatch回滚,已执行的Subbatch不回滚】
     * 用切面将之前的 Batch SQL "切片"操作后, 没有事务情况下, 已执行的"切片"不会回滚.
     * 注意：只要一条记录异常,当前的"切片"都会回滚, 因为batch SQL本身是原子性操作. 已执行的"切片"不会回滚.
     */
    @GetMapping(value = "/update/batch/aop/exception")
    public String batchException(int num) {
        List<Student> list = new ArrayList<>();
        List<Student> selectAll = studentService.selectAll();
        // 对数据库的前num个数进行修改,注意数据库必须有数据,切面默认切分list的sublist大小为5
        for (int i = 0; i < num; i++) {
            BigDecimal id = selectAll.get(i).getId();
            Student student = getStudent(id + "");
            student.setAge(BigDecimal.valueOf(num));
            list.add(student);
        }
        // 制造异常
        Student student = list.get(num - 1);
        student.setId(null);
        int i = studentService.updateListByIdBatchAop(list);
        logger.info("Mybatis SQL return :" + i);
        return i + "";
    }

    /**
     * AOP注解声明式事务 【事务无效】
     * Spring的注解事务是通过代理对象生效的,对象自我调用没有触发Spring代理机制
     * 原理同Bean.a()调用事务方法Bean.b(),b()事务无效]一样
     * AOP注解声明式事务 —— 对AOP切面不生效,切面出现异常,已执行的"切片"不会回滚. 相当于AOP没有事务
     */
    @GetMapping(value = "/update/batch/aoptrans/exception")
    public String updateListByIdBatchAopHaveTrans(int num) {
        List<Student> list = new ArrayList<>();
        List<Student> selectAll = studentService.selectAll();
        // 对数据库的前num个数进行修改,注意数据库必须有数据,切面默认切分list的sublist大小为5
        for (int i = 0; i < num; i++) {
            BigDecimal id = selectAll.get(i).getId();
            Student student = getStudent(id + "");
            student.setAge(BigDecimal.valueOf(num));
            list.add(student);
        }
        Student student = list.get(num - 1);
        student.setId(null);
        int i = studentService.updateListByIdBatchAopHaveTrans(list);
        logger.info("Mybatis SQL return :" + i);
        return i + "";
    }


    /**
     * Service无事务, AOP有事务(编程式)
     * 切面事务生效,这里事务级别 REQUIRE_NEW,切面出现异常且未捕获处理,已执行的"切片"全部回滚
     */
    @GetMapping(value = "/update/batch/aoptransself/exception")
    public String updateListByIdBatchAopHaveTransSelf(int num) {
        List<Student> list = new ArrayList<>();
        List<Student> selectAll = studentService.selectAll();
        // 对数据库的前num个数进行修改,注意数据库必须有数据,切面默认切分list的sublist大小为5
        for (int i = 0; i < num; i++) {
            BigDecimal id = selectAll.get(i).getId();
            Student student = getStudent(id + "");
            student.setAge(BigDecimal.valueOf(num));
            list.add(student);
        }
        // 制造异常
        Student student = list.get(num - 1);
        student.setId(null);
        int i = studentService.updateListByIdBatchAopHaveTransSelf(list);
        logger.info("Mybatis SQL return :" + i);
        return i + "";
    }

    /**
     * 【切面事务推荐写法】
     * Service有事务, AOP有事务(编程式)
     * 切面事务生效,这里事务级别 REQUIRE_NEW,切面出现异常在业务层捕获了,不会业务层的事务. 效果同上
     */
    @GetMapping(value = "/update/batch/transaoptransself/exception")
    public String updateListByIdBatchTransAopHaveTransSelf(int num, boolean exception) {
        List<Student> list = new ArrayList<>();
        List<Student> selectAll = studentService.selectAll();
        // 对数据库的前num个数进行修改,注意数据库必须有数据,切面默认切分list的sublist大小为5
        for (int i = 0; i < num; i++) {
            BigDecimal id = selectAll.get(i).getId();
            Student student = getStudent(id + "");
            student.setAge(BigDecimal.valueOf(num));
            list.add(student);
        }
        // 制造异常
        if (exception) {
            Student student = list.get(num - 1);
            student.setId(null);
        }
        int i = studentService.updateListByIdBatchTransAopHaveTransSelf(list);
        logger.info("Mybatis SQL return :" + i);
        return i + "";
    }

    // ---------------------------------- 其他测试 ----------------------------------

    /**
     * Service有事务,AOP无事务, 异常不处理,触发事务回滚, service和切面全部回滚 (默认REQUIRE级别)
     * 同正常事务逻辑
     */
    @GetMapping(value = "/update/batch/aop/exception/trans")
    public String t6(int num) {
        List<Student> list = new ArrayList<>();
        List<Student> selectAll = studentService.selectAll();
        // 对数据库的前num个数进行修改,注意数据库必须有数据,切面默认切分list的sublist大小为5
        for (int i = 0; i < num; i++) {
            BigDecimal id = selectAll.get(i).getId();
            Student student = getStudent(id + "");
            student.setAge(BigDecimal.valueOf(num));
            list.add(student);
        }
        Student student = list.get(num - 1);
        student.setId(null);
        int i = studentService.updateListByIdBatchTransAop(list);
        logger.info("Mybatis SQL return :" + i);
        return i + "";
    }

    /**
     * Service有事务,AOP无事务,但是Service捕获切面的异常, 相当于事务不生效, AOP已执行的"切片"不会回滚.
     * 同正常事务逻辑
     */
    @GetMapping(value = "/update/batch/aop/exception/trans/catch")
    public String t7(int num) {
        List<Student> list = new ArrayList<>();
        List<Student> selectAll = studentService.selectAll();
        // 对数据库的前num个数进行修改,注意数据库必须有数据,切面默认切分list的sublist大小为5
        for (int i = 0; i < num; i++) {
            BigDecimal id = selectAll.get(i).getId();
            Student student = getStudent(id + "");
            student.setAge(BigDecimal.valueOf(num));
            list.add(student);
        }
        Student student = list.get(num - 1);
        student.setId(null);
        int i = studentService.updateListByIdBatchTransCatchExceptionAop(list);
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
