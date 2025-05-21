package com.inter.dao;

import com.BaseJunit;
import com.alibaba.fastjson.JSON;
import com.dao.StudentPlusMapper;
import com.entity.Student;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class StudentPlusMapperTest extends BaseJunit {

    @Autowired
    private StudentPlusMapper studentPlusMapper;

    @Test
    public void test1() {
        Student student = studentPlusMapper.selectById(1);
        log.info(JSON.toJSONString(student));
    }
}