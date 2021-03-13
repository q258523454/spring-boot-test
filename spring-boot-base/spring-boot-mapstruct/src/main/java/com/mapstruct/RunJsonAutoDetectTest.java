package com.mapstruct;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapstruct.entity.JacksonEntity1;
import com.mapstruct.entity.JacksonEntity2;
import com.mapstruct.entity.JacksonEntity3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @Description
 * @date 2020-03-23 20:14
 * @modify
 */
public class RunJsonAutoDetectTest {

    private static final Logger logger = LoggerFactory.getLogger(RunJsonAutoDetectTest.class);

    public static void main(String[] args) throws IOException {
        JacksonEntity1 entity1 = new JacksonEntity1();
        entity1.setZhangId(1);
        entity1.setZhangName("1");
        entity1.setZhangAge(1);
        entity1.setxBusCod("1");

        JacksonEntity2 entity2 = new JacksonEntity2();
        entity2.setId(2);
        entity2.setName("2");
        entity2.setAge(2);
        entity2.setXBusCod("2");

        JacksonEntity3 entity3 = new JacksonEntity3();
        entity3.setId(3);
        entity3.setName("3");
        entity3.setAge(3);
        entity3.setXBusCod("3");


        ObjectMapper objectMapper = new ObjectMapper();
        logger.info("jackson entity1:" + objectMapper.writeValueAsString(entity1));
        logger.info("jackson entity2:" + objectMapper.writeValueAsString(entity2));
        logger.info("jackson entity3:" + objectMapper.writeValueAsString(entity3));

        logger.info("fastJson entity2:" + JSON.toJSONString(entity1));
        logger.info("fastJson entity2:" + JSON.toJSONString(entity2));
        logger.info("fastJson entity3:" + JSON.toJSONString(entity3));


        // {"id":3,"name":"3","age":3,"xbusCod":"3"}
        String str = "{\"id\":3,\"name\":\"3\",\"age\":3,\"xbusCod\":\"3\"}";
        logger.info(objectMapper.readValue(str, JacksonEntity2.class).toString());
        // JsonAutoDetectEntity3只支持同名字段, xBusCod 而不是 xbusCod
        logger.info(objectMapper.readValue(str, JacksonEntity3.class).toString());
    }
}
