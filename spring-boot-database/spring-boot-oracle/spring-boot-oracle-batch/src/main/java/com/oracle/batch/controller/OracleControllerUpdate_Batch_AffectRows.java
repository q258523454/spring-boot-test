package com.oracle.batch.controller;

import com.oracle.batch.entity.Student;
import com.oracle.batch.service.IOracleStudentService;
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
public class OracleControllerUpdate_Batch_AffectRows {
    private static final Logger logger = LoggerFactory.getLogger(OracleControllerUpdate_Batch_AffectRows.class);

    @Autowired
    private IOracleStudentService studentService;

    /**
     * update batch{foreach + case when} 格式可以返回影响行数
     * 注意: 同insert一样. begin end 无法返回更新行数
     */
    @GetMapping(value = "/affect/rows/update/batch")
    public String batch(int num) {
        List<Student> list = new ArrayList<>();
        for (int i = 1; i <= num; i++) {
            Student student = getStudent(i + "");
            student.setAge(BigDecimal.valueOf(num));
            list.add(student);
        }
        int i = studentService.updateListByIdBatch(list);
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
