package com.trans.service;

import com.trans.entity.Student;

import java.util.List;

/**
 * Created By
 *
 * @author :   zhangj
 * @date :   2019-02-22
 */
public interface StudentService {
    public List<Student> selectAllStudent();

    public int insertStudent(Student student);

    List<Student> selectAllByUsername(String username);

    int updateById(Student updated, Long id);

    Student selectById(Long id);

}
