package com.rediscache.service.impl;

import com.alibaba.fastjson.JSON;
import com.rediscache.dao.StudentMapper;
import com.rediscache.entity.Student;
import com.rediscache.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Date: 2019-06-11
 * @Version 1.0
 */
@Service
public class StudentServiceImpl implements StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Autowired
    private StudentMapper studentMapper;



    /**
     * cacheNames="" 跟 value="" 一样
     */
    @Override
    public Integer insertStudent(Student student) {
        return studentMapper.insertStudent(student);
    }

    @Override
    @Cacheable(value = "student", key = "'stu'.concat(#id.toString())")
    // @Cacheable(value = "student", key = "'stu'.concat(#id.toString())",unless = "#result==null")
    public Student selectStudentById(int id) {
        return studentMapper.selectStudentById(id);
    }

    @Override
    @Cacheable(value = "student", key = "'stu.all'")
    public List<Student> selectAllStudent() {
        return studentMapper.selectAllStudent();
    }

    @Override
    @CachePut(cacheNames = "student", key = "'stu'.concat(#student.id.toString())")
    public Student updateStudent(Student student) {
        log.info("update student: {}", JSON.toJSONString(student));
        studentMapper.updateStudent(student);
        return student;
    }

    @Override
    @CacheEvict(cacheNames = "student", key = "'stu'.concat(#id.toString())")
    public Integer deleteStudentById(int id) {
        return studentMapper.deleteStudentById(id);
    }
}
