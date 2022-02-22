package com.rediscache.dao;

import com.rediscache.entity.Student;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created By
 *
 * @date :   2018-08-31
 */

@Repository
public interface StudentMapper {

    Integer insertStudent(Student student);

    Integer deleteStudentById(int id);

    Integer updateStudent(Student student);

    Student selectStudentById(int id);

    List<Student> selectAllStudent();

}
