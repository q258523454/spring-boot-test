package com.batch.controller.afftect_rows;

import com.batch.entity.Student;
import com.batch.service.IOracleStudentService;
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
public class T2_Insert_Batch_AffectRows {
    private static final Logger logger = LoggerFactory.getLogger(T2_Insert_Batch_AffectRows.class);

    @Autowired
    private IOracleStudentService studentService;

    /**
     * --------------------------------------------------------------------------------------------------
     *  总结:
     *  default-executor-type: simple
     *  1. insert batch foreach 可以返回更新行数
     *  2. update batch foreach 可以返回更新行数
     *  3. begin-end 无法返回行数
     * --------------------------------------------------------------------------------------------------
     */

    /**
     * insert batch{foreach}
     * default-executor-type: simple(Mybatis默认) 可以返回更新行数
     */
    @GetMapping(value = "/affect/rows/insert/batch")
    public String insertList() {
        List<Student> studentList = new ArrayList<>();
        long maxId = 1;
        if (null != studentService.getMaxId()) {
            maxId = studentService.getMaxId() + 1;
        }
        for (int i = 0; i < 2; i++) {
            long pId = maxId + i;
            Student student = getStudent(pId + "");
            studentList.add(student);
        }
        int i = studentService.insertListBatch(studentList);
        logger.info("Mybatis SQL return :" + i);
        return i + "";
    }

    // begin end 无法返回更新行数
    @GetMapping(value = "/affect/rows/insert/beginend")
    public String insertListBeginEnd() {
        List<Student> studentList = new ArrayList<>();
        long maxId = 1;
        if (null != studentService.getMaxId()) {
            maxId = studentService.getMaxId() + 1;
        }
        for (int i = 0; i < 2; i++) {
            long pId = maxId + i;
            Student student = getStudent(pId + "");
            studentList.add(student);
        }
        int i = studentService.insertListBeginEnd(studentList);
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
