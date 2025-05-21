package com.trans.service;

import com.trans.entity.Student;

import java.util.List;


public interface StudentService {
    public List<Student> selectAllStudent();

    public int insertStudent(Student student);

    public int insertStudentTrans(Student student);

    public int transactionRequiresNew();

    List<Student> selectAllByUsername(String username);

    int updateById(Student updated, Long id);

    Student selectById(Long id);

}
