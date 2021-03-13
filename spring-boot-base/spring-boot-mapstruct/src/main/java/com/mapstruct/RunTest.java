package com.mapstruct;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;


import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapstruct.entity.StudentA;
import com.mapstruct.entity.StudentB;
import com.mapstruct.mapping.StudentMapping;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @Description
 * @date 2020-03-13 16:19
 * @modify
 */
public class RunTest {
    private static final Logger logger = LoggerFactory.getLogger(RunTest.class);

    public static void main(String[] args) throws JsonProcessingException {

        StudentA studentA = new StudentA();
        studentA.setName("aaa");
        studentA.setVersion("bb");
        studentA.setAge(11);
        studentA.setDate(new Date());
        studentA.setFlag("cc");
        studentA.setZDbkNbr("ddd");
        ObjectMapper map = new ObjectMapper();
        // Jackson 默认按 get/set 序列化
        System.out.println("studentA: Jackson默认序列化" + map.writeValueAsString(studentA));
        System.out.println("studentA: FastJson序列化" + JSON.toJSONString(studentA));


        StudentB studentB = new StudentB();
        StudentMapping mapping = Mappers.getMapper(StudentMapping.class);
        studentB = mapping.studentAToStudentB(studentA);

        logger.info(JSON.toJSONString(studentB));

    }

    public static void convert(StudentA x1, StudentB z1) {
        StudentMapping mapping = Mappers.getMapper(StudentMapping.class);
        z1 = mapping.studentAToStudentB(x1);
    }
}
