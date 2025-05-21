package com.inter.service;

import com.inter.entity.Teacher;

import java.util.List;


public interface TeacherService {
    List<Teacher> selectAllTeacher();

    int insertTeacher(Teacher teacher);
}
