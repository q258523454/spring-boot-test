package com.oracle.vs.mybatis.service.impl;

import com.oracle.vs.mybatis.dao.OracleStudentMapper;
import com.oracle.vs.mybatis.entity.Student;
import com.oracle.vs.mybatis.service.IOracleStudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;



@Service
public class OracleStudentServiceImpl implements IOracleStudentService {
    private static final Logger logger = LoggerFactory.getLogger(OracleStudentServiceImpl.class);

    @Autowired
    protected OracleStudentMapper studentMapper;

    @Override
    public Long getMaxId() {
        return studentMapper.getMaxId();
    }

    @Override
    public Student selectByPrimaryKey(BigDecimal id) {
        return studentMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Student> selectByName(Student student) {
        return studentMapper.selectByName(student);
    }

    @Override
    public List<Student> selectAll() {
        return studentMapper.selectAll();
    }

    @Override
    public int insertListBatch(List<Student> studentList) {
        return studentMapper.insertListBatch(studentList);
    }

    @Override
    public int insert(Student record) {
        return studentMapper.insert(record);
    }

    @Override
    public int update(Student record) {
        return studentMapper.update(record);
    }

    @Override
    public int updateListByIdBeginEnd(List<Student> studentList) {
        return studentMapper.updateListByIdBeginEnd(studentList);
    }

    @Override
    public int updateListByIdBatch(List<Student> studentList) {
        return studentMapper.updateListByIdBatch(studentList);
    }

    @Override
    public int insertListBeginEnd(List<Student> studentList) {
        return studentMapper.insertListBeginEnd(studentList);
    }

    @Override
    public int updateById(Student updated, BigDecimal id) {
        return studentMapper.updateById(updated, id);
    }

    @Override
    public int updateByName(Student updated, String name) {
        return studentMapper.updateByName(updated, name);
    }
}
