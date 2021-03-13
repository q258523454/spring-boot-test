package com.rediscache.service;


import com.rediscache.entity.Student;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created By
 *
 * @date :   2018-08-28
 */

public interface StudentService {

    Integer insertStudent(Student student);

    Integer deleteStudentById(int id);

    Student updateStudent(Student student);

    Student selectStudentById(int id);

    List<Student> selectAllStudent();
}
