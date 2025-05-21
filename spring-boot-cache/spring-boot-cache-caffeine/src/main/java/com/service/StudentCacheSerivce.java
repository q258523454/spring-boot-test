package com.inter.service;

import com.inter.entity.Student;

import java.util.List;

/**
 * @Date: 2019-05-10
 * @Version 1.0
 */
public interface StudentCacheSerivce {

    Integer insertStudent(Student student);

    Integer deleteStudentById(int id);

    Student updateStudent(Student student);

    Student selectStudentById(int id);

    List<Student> selectAllStudent();

}
