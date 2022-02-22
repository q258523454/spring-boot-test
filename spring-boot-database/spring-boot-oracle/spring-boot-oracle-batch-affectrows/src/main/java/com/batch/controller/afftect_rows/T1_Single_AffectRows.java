package com.batch.controller.afftect_rows;

import com.batch.entity.Student;
import com.batch.service.IOracleStudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @Author: zhangj
 * @Date: 2019-12-16
 * @Version 1.0
 */
@RestController
public class T1_Single_AffectRows {
    private static final Logger logger = LoggerFactory.getLogger(T1_Single_AffectRows.class);

    @Autowired
    private IOracleStudentService studentService;

    /**
     * 总结:
     * 增删改返回的都是影响行数,affect rows.
     *
     * 区别:
     * insert 返回值，默认情况下不存在返回0
     * delete/update 返回值存在0(无数据删除/修改)
     * 成功情况都返回1
     *
     * mybatis insert
     * 1.返回类型一般写 int
     * 2.插入成功: =1
     *   插入失败: 抛异常
     *
     *
     * mybatis delete/update:
     * 1.返回类型一般写 int
     * 2.删除成功: =1
     *   删除失败:
     *      无数据删除: =0
     *      删除异常: 抛异常
     *
     */

    /**
     * mybatis insert:
     * TODO: 网上说的等于0,是什么场景会出现的？
     */
    @GetMapping(value = "/student/insert")
    public String insert(Integer id) {
        long maxId = 0;
        if (null != id) {
            maxId = id;
        } else {
            maxId = studentService.getMaxId() + 1;
        }
        Student student = getStudent(maxId + "");
        int i = studentService.insert(student);
        logger.info("Mybatis SQL return :" + i);
        return i + "";
    }


    /**
     * mybatis delete:
     * 1.返回类型一般写 int
     * 2.删除成功: =1
     *   删除失败:
     *      无数据删除: =0
     *      删除异常: 抛异常
     */
    @GetMapping(value = "/student/delete")
    public String delete(Integer id) {
        if (null == id) {
            return "id must be not null";
        }
        int i = studentService.deleteById(id + "");
        logger.info("Mybatis SQL return :" + i);
        return i + "";
    }

    /**
     * mybatis update:
     * 1.返回类型一般写 int
     * 2.修改成功: =1
     *   修改失败:
     *      无数据修改: =0
     *      修改异常: 抛异常
     */
    @GetMapping(value = "/student/update")
    public String update(Integer id) {
        if (null == id) {
            return "id must be not null";
        }
        Student student = new Student();
        student.setName(UUID.randomUUID().toString().substring(0, 3));
        int i = studentService.updateById(student, BigDecimal.valueOf(id));
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
