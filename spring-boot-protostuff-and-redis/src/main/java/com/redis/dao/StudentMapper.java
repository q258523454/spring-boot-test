package com.redis.dao;

import com.redis.entity.Student;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created By
 *
 * @author :   zhangjian
 * @date :   2018-08-31
 */

@Repository
public interface StudentMapper {

    Integer insertStudent(Student student);

    Integer deleteStundetById(int id);

    Integer updateStudent(Student student);

    Student selectStudentById(int id);

    List<Student> selectAllStuddent();

}
