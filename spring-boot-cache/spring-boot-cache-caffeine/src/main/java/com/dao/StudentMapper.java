package com.inter.dao;

import com.inter.entity.Student;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StudentMapper {
    List<Student> selectAll();


    Integer insertStudent(Student student);

    Integer deleteStudentById(int id);

    Integer updateStudent(Student student);

    Student selectStudentById(int id);

    List<Student> selectAllStudent();
}