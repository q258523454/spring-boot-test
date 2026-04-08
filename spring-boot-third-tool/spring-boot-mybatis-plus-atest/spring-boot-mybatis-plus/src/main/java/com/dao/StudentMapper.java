package com.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.entity.Student;

import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StudentMapper extends BaseMapper<Student> {
    List<Student> selectAll();
}