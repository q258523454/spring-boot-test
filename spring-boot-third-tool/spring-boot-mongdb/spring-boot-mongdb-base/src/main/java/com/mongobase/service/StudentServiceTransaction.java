package com.mongobase.service;

import com.mongobase.pojo.entity.Student;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class StudentServiceTransaction {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void insertAllNoTrans(List<Student> list1,List<Student> list2) {
        mongoTemplate.insertAll(list1);
        throw new RuntimeException("error");
    }

    @Transactional
    public void insertAllTrans(List<Student> list1,List<Student> list2) {
        mongoTemplate.insertAll(list1);
        throw new RuntimeException("error");
    }
}
