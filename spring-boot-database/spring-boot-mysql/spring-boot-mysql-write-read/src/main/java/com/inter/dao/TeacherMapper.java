package com.inter.dao;

import com.inter.entity.Teacher;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TeacherMapper {
    public List<Teacher> selectAllTeacher();

    public int insertTeacher(Teacher teacher);
}
