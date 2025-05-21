package com.mysql.level.serivce.impl;

import com.mysql.level.dao.StudentMapper;
import com.mysql.level.entity.Student;
import com.mysql.level.serivce.IStudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Service
public class MysqlStudentServiceImpl implements IStudentService {
    private static final Logger logger = LoggerFactory.getLogger(MysqlStudentServiceImpl.class);

    @Autowired
    protected StudentMapper studentMapper;

    @Override
    public List<Student> selectAll() {
        return studentMapper.selectAll();
    }

    @Override
    public int insert(Student record) {
        return studentMapper.insert(record);
    }

    @Override
    public Student selectByPrimaryKey(Integer id) {
        return studentMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByName(Student updated, String name) {
        return studentMapper.updateByName(updated, name);
    }

    @Override
    public List<Student> selectByName(String name) {
        return studentMapper.selectByName(name);
    }
}
