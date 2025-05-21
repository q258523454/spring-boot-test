package com.trans.service.impl;

import com.trans.dao.TeacherMapper;
import com.trans.entity.Teacher;
import com.trans.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherMapper mapper;


    @Override
    public List<Teacher> selectAllTeacher() {
        return mapper.selectAllTeacher();
    }

    @Override
    public int insertTeacher(Teacher teacher) {
        return mapper.insertTeacher(teacher);
    }
}
