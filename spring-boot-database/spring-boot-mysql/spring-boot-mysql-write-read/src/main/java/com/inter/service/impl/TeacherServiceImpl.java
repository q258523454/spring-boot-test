package com.inter.service.impl;

import com.inter.dao.TeacherMapper;
import com.datasource.annotation.DataSourceAnnotation;
import com.datasource.entity.DataSourceEnum;
import com.inter.entity.Teacher;
import com.inter.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherMapper mapper;


    @Override
    @DataSourceAnnotation(DataSourceEnum.SLAVE)
    public List<Teacher> selectAllTeacher() {
        return mapper.selectAllTeacher();
    }

    @Override
    @DataSourceAnnotation(DataSourceEnum.SLAVE)
    public int insertTeacher(Teacher teacher) {
        return mapper.insertTeacher(teacher);
    }
}
