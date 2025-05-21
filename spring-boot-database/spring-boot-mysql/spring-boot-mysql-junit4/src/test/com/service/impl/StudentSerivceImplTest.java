package com.inter.service.impl;

import com.SpringBootMybatisApplication;
import com.alibaba.fastjson.JSON;
import com.inter.dao.StudentMapper;
import com.inter.entity.Student;
import com.inter.service.StudentSerivce;
import com.sun.prism.j2d.J2DPipeline;
import com.util.SpringContextHolder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootMybatisApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentSerivceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(StudentSerivceImpl.class);

    @Autowired
    private StudentMapper mapper;

    @Test
    public void selectAll() {
        logger.warn("ok");
        logger.warn(JSON.toJSONString(mapper.selectAll()));
    }


    @Test
    public void printBean() {
        String[] beanNameList = SpringContextHolder.getApplicationContext().getBeanDefinitionNames();
        Arrays.sort(beanNameList);
        for (String bean : beanNameList) {
            System.out.println(bean + " of Type :: " + SpringContextHolder.getApplicationContext().getBean(bean).getClass());
        }
        StudentSerivce studentSerivce = SpringContextHolder.getBean("studentSerivceImpl");
        studentSerivce.selectAll();
    }

}