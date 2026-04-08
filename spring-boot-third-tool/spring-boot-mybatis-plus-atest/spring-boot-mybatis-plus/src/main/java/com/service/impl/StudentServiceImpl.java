package com.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dao.StudentMapper;
import com.entity.Student;
import com.service.StudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public List<Student> selectAll() {
        return studentMapper.selectAll();
    }

}
