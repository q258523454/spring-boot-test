package com.service.impl;

import com.dao.StudentMapper;
import com.entity.Student;
import com.service.StudentSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Date: 2019-05-10
 * @Version 1.0
 */

@Service
public class StudentSerivceImpl implements StudentSerivce {

    @Autowired
    private StudentMapper mapper;

    @Override
    public Integer insertStudent(Student student) {
        return mapper.insertStudent(student);
    }

    @Override
    public Integer deleteStudentById(Integer id) {
        return mapper.deleteStudentById(id);
    }

    @Override
    public Student updateStudent(Student student) {
        Integer res = mapper.updateStudent(student);
        if (null != res) {
            return student;
        } else {
            return null;
        }
    }

    @Override
    public Student selectStudentById(Integer id) {
        return mapper.selectStudentById(id);
    }
}
