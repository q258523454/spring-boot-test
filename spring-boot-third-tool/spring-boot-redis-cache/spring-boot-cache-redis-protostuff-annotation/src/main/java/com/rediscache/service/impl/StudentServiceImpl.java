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

    /**
     * Cacheable
     * 先查询缓存中是否存在,存在则直接返回,不存在,则查询数据返回,并将数据缓存.
     * cacheNames —— 表示缓存在内存的分组名（文件夹），便于匹配
     * unless —— 条件不符合则不缓存
     */
    @Override
    @Cacheable(cacheNames = "MyCacheStore", key = "'stu'.concat(#id.toString())", cacheManager = "cacheManager")
    // @Cacheable(value = "student", key = "'stu'.concat(#id.toString())",unless = "#result==null")
    public Student selectStudentById(int id) {
        return studentMapper.selectStudentById(id);
    }

    @Override
    @Cacheable(cacheNames = "MyCacheStore", key = "'stu.all'")
    public List<Student> selectAllStudent() {
        return studentMapper.selectAllStudent();
    }

    /**
     * CachePut
     * 新增或修改方法,返回值Put到缓存中, 供其他查询使用.
     */
    @Override
    @CachePut(cacheNames = "MyCacheStore", key = "'stu'.concat(#student.id.toString())")
    public Student updateStudent(Student student) {
        log.info("update student: {}", JSON.toJSONString(student));
        studentMapper.updateStudent(student);
        return student;
    }

    /**
     * CacheEvict
     * 删除或修改方法,清空指定缓存.
     */
    @Override
    @CacheEvict(cacheNames = "MyCacheStore", key = "'stu'.concat(#id.toString())")
    public Integer deleteStudentById(int id) {
        return studentMapper.deleteStudentById(id);
    }
}
