package com.inter.service;

import com.inter.entity.Student;

import java.util.List;


public interface StudentService {
    public Integer selectAllCount();

    //public Page<Student> selectAllStudent();

    public List<Student> selectAllStudent();

    public int insertStudent(Student student);
}
