package com.mysql.affectrows.serivce.impl;

import com.mysql.affectrows.dao.StudentMapper;
import com.mysql.affectrows.entity.Student;
import com.mysql.affectrows.serivce.IStudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: zhangj
 * @Date: 2019-12-16
 * @Version 1.0
 */

@Service
public class MysqlStudentServiceImpl implements IStudentService {
    private static final Logger logger = LoggerFactory.getLogger(MysqlStudentServiceImpl.class);

    @Autowired
    protected StudentMapper studentMapper;

    @Override
    public Long getMaxId() {
        return studentMapper.getMaxId();
    }

    @Override
    public List<Student> selectAll() {
        return studentMapper.selectAll();
    }

    @Override
    public int selectMaxId() {
        return studentMapper.selectMaxId();
    }

    @Override
    @Transactional(timeout = 3)
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

    @Override
    public int insertListBatch(List<Student> studentList) {
        return studentMapper.insertListBatch(studentList);
    }

    @Override
    public int updateListByIdBatch(List<Student> studentList) {
        return studentMapper.updateListByIdBatch(studentList);
    }

    @Override
    public int updateListByIdAndIdBatch(List<Student> studentList) {
        return studentMapper.updateListByIdAndIdBatch(studentList);
    }

    @Override
    public int deleteByIdList(List<String> list) {
        return studentMapper.deleteByIdList(list);

    }
}
