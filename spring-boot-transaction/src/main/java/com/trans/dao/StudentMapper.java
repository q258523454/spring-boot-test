package com.trans.dao;

import com.trans.entity.Student;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StudentMapper {
    public List<Student> selectAllStudent();

    public int insertStudent(Student student);

    List<Student> selectAllByUsername(@Param("username") String username);

    int updateById(@Param("updated") Student updated, @Param("id") Long id);

    Student selectById(@Param("id") Long id);

}

