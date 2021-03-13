package com.service;

import com.entity.Student;

import java.util.List;

/**
 * @Date: 2019-05-10
 * @Version 1.0
 */
public interface StudentSerivce {

    // 新增
    public Integer insertStudent(Student student);

    // 删除
    public Integer deleteStudentById(Integer id);

    // 更新
    public Student updateStudent(Student student);

    // 查询
    public Student selectStudentById(Integer id);

}
