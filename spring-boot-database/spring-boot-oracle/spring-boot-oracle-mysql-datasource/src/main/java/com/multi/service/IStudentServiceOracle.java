package com.multi.service;


import com.multi.entity.oracle.Student;

import java.util.List;


public interface IStudentServiceOracle {
    List<Student> selectAll();

    int insert(Student record);

}
