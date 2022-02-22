package com.batch.controller;

import com.batch.dao.Student3Mapper;
import com.batch.entity.Student3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @Author: zhangj
 * @Date: 2019-12-16
 * @Version 1.0
 */

@RestController
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private Student3Mapper student3Mapper;

    private Random random = new Random();


    @Transactional // 对速度基本无影响
    @GetMapping(value = "/effective/student3/insert/batch")
    public String insertList2(int num) {
        long start = System.currentTimeMillis();
        Long maxId = student3Mapper.getMaxId();
        long tempMaxId = (maxId == null) ? 0 : maxId + 1;

        List<Student3> studentList = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            long pId = tempMaxId + i;
            Student3 student = getStudent(pId + "");
            studentList.add(student);
        }
        logger.info("Mybatis SQL return :" + student3Mapper.batchInsert(studentList));
        long end = System.currentTimeMillis();
        logger.info("批量插入 执行时间:" + (end - start));
        return "ok";
    }

    public Student3 getStudent(String id) {
        Student3 student = new Student3();
        student.setId(Long.valueOf(id));
        student.setName(UUID.randomUUID().toString().substring(0, 3));
        student.setAge(random.nextInt(9999));
        student.setName2("Name");
        student.setName3("Name");
        student.setName4("Name");
        student.setName5("Name");
        student.setName6("Name");
        student.setName7("Name");
        student.setName8("Name");
        student.setName9("Name");
        student.setName10("Name");
        student.setName11("Name");
        student.setName12("Name");
        student.setName13("Name");
        student.setName14("Name");
        student.setName15("Name");
        student.setName16("Name");
        student.setName17("Name");
        student.setName18("Name");
        student.setName19("Name");
        student.setName20("Name");
        student.setName21("Name");
        student.setName22("Name");
        student.setName23("Name");
        student.setName24("Name");
        student.setName25("Name");
        student.setName26("Name");
        student.setName27("Name");
        student.setName28("Name");
        student.setName29("Name");
        student.setName30("Name");
        student.setName31("Name");
        student.setName32("Name");
        student.setName33("Name");
        student.setName34("Name");
        student.setName35("Name");
        student.setName36("Name");
        student.setName37("Name");
        student.setName38("Name");
        student.setName39("Name");
        student.setName40("Name");
        student.setName41("Name");
        student.setName42("Name");
        student.setName43("Name");
        student.setName44("Name");
        student.setName45("Name");
        student.setName46("Name");
        student.setName47("Name");
        return student;
    }
}
