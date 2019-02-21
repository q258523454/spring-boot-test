package com.redis.service;


import com.redis.entity.Student;

import java.util.List;

/**
 * Created By
 *
 * @author :   zhangjian
 * @date :   2018-08-28
 */


public interface StudentService {
    public Student insertStudent(Student student);

    public Integer transcationInsertStudent(Student student1, Student student2);

    Integer deleteStundetById(int id);

    Integer updateStudent(Student student);

    Student selectStudentById(int id);

    List<Student> selectAllStuddent();
}
