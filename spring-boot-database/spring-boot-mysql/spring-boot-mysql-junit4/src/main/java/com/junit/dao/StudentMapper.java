package com.junit.dao;

import com.junit.entity.Student;

import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StudentMapper {
    List<Student> selectAll();
}