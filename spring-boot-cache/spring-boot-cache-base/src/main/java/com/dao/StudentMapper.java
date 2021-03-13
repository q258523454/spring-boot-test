package com.dao;

import com.entity.Student;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StudentMapper {
    List<Student> selectAll();


    // 新增
    public Integer insertStudent(Student student);

    // 删除
    public Integer deleteStudentById(Integer id);

    // 更新
    public Integer updateStudent(Student student);

    // 查询
    public Student selectStudentById(Integer id);
}