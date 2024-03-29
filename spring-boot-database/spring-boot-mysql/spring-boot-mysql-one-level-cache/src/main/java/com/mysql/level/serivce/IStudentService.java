package com.mysql.level.serivce;


import com.mysql.level.entity.Student;

import java.util.List;

/**
 * @Description
 * @date 2020-03-12 22:01
 * @modify
 */
public interface IStudentService {


    List<Student> selectAll();

    int insert(Student record);

    Student selectByPrimaryKey(Integer id);

    int updateByName(Student updated, String name);

    List<Student> selectByName(String name);
}
