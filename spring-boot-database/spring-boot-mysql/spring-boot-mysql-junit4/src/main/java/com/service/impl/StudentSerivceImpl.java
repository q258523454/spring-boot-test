package com.inter.service.impl;

import com.inter.dao.StudentMapper;
import com.inter.entity.Student;
import com.inter.service.StudentSerivce;
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
    public List<Student> selectAll() {
        return mapper.selectAll();
    }

}
