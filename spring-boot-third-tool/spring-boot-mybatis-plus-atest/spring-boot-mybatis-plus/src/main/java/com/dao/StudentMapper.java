package com.dao;


import com.entity.Student;

import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StudentMapper {
    List<Student> selectAll();
}