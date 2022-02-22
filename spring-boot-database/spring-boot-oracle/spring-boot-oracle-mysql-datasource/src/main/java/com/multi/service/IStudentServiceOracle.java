package com.multi.service;


import com.multi.entity.oracle.Student;

import java.util.List;

/**
 * @Author: zhangj
 * @Date: 2019-12-16
 * @Version 1.0
 */
public interface IStudentServiceOracle {
    List<Student> selectAll();

    int insert(Student record);

}
