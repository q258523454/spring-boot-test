package com.multi.service;



import com.multi.entity.mysql.Student;

import java.util.List;


public interface IStudentServiceMysql {
    List<Student> selectAll();

    int insert(Student record);

}
