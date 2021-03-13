package com.service.impl;

import com.alibaba.fastjson.JSON;
import com.dao.StudentMapper;
import com.entity.Student;
import com.service.StudentCacheSerivce;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Date: 2019-05-10
 * @Version 1.0
 */

@Service
public class StudentCacheSerivceImpl implements StudentCacheSerivce {

    private static final Logger log = LoggerFactory.getLogger(StudentCacheSerivceImpl.class);


    @Autowired
    private StudentMapper studentMapper;

    /**
     * cacheNames="" 跟 value="" 一样
     */
    @Override
    public Integer insertStudent(Student student) {
        return studentMapper.insertStudent(student);
    }


    /**
     * 注意是 OTHER 组, 有不同的过期时间和缓存数量配置
     */
    @Override
    @Cacheable(value = "OTHER", key = "'stu.all'")
    public List<Student> selectAllStudent() {
        return studentMapper.selectAllStudent();
    }

    @Override
    @Cacheable(value = "STUDENT", key = "'stu'.concat(#id.toString())")
    // @Cacheable(value = "student", key = "'stu'.concat(#id.toString())",unless = "#result==null")
    public Student selectStudentById(int id) {
        return studentMapper.selectStudentById(id);
    }

    @Override
    @CachePut(cacheNames = "STUDENT", key = "'stu'.concat(#student.id.toString())")
    public Student updateStudent(Student student) {
        log.info("update student: {}", JSON.toJSONString(student));
        studentMapper.updateStudent(student);
        return student;
    }

    @Override
    @CacheEvict(cacheNames = "STUDENT", key = "'stu'.concat(#id.toString())")
    public Integer deleteStudentById(int id) {
        return studentMapper.deleteStudentById(id);
    }

}
