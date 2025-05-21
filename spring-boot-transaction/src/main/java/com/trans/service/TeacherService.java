package com.trans.service;

import com.trans.entity.Teacher;

import java.util.List;


public interface TeacherService {
    List<Teacher> selectAllTeacher();

    int insertTeacher(Teacher teacher);
}
