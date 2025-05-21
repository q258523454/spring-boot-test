package com.inter.service.impl;

import com.inter.dao.StudentMapper;
import com.datasource.annotation.DataSourceAnnotation;
import com.datasource.entity.DataSourceEnum;
import com.inter.entity.Student;
import com.inter.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper mapper;

    @Override
    public Integer selectAllCount() {
        return mapper.selectAllCount();
    }

    @Override
    //@DataSourceAnnotation(DataSourceEnum.Master)
    // 默认是master数据库
    public List<Student> selectAllStudent() {
        return mapper.selectAllStudent();
    }

    @Override
    @DataSourceAnnotation(DataSourceEnum.MASTER)
    public int insertStudent(Student student) {
        return mapper.insertStudent(student);
    }

}
