package com.quartz.dao;


import com.quartz.entity.Student;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StudentMapper {
    List<Student> selectAll();
}