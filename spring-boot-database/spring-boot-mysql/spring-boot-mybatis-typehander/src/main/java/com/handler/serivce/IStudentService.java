package com.handler.serivce;

import com.handler.entity.Student;

import java.util.List;

/**
 * @Description
 * @date 2020-03-12 22:01
 * @modify
 */
public interface IStudentService {


    List<Student> selectAll();

    int insert(Student record);

    int updateByName(Student updated, String name);

}
