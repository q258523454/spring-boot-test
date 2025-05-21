package com.inter.dao;

import com.inter.entity.Student;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StudentMapper {


    Integer selectAllCount();

    // public Page<Student> selectAllStudent();

    public List<Student> selectAllStudent();

    public int insertStudent(Student student);

}

