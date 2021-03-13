package com.mysql.timeout.serivce.impl;

import com.mysql.timeout.dao.StudentMapper;
import com.mysql.timeout.entity.Student;
import com.mysql.timeout.serivce.IStudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
    public List<Student> selectAll() {
        return studentMapper.selectAll();
    }

    @Override
    public int selectMaxId() {
        return studentMapper.selectMaxId();
    }

    @Override
    public int insert(Student record) {
        return studentMapper.insert(record);
    }

    @Override
    // 设置超时时间3秒,内部超时-有效
    @Transactional(timeout = 2)
    public int insertTimeout(Student record) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return studentMapper.insert(record);
    }

    @Override
    // 设置超时时间3秒,内部超时-无效
    @Transactional(timeout = 2)
    public int insertTimeoutNotWork1(Student record) {
        int insert = studentMapper.insert(record);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return insert;
    }

    @Override
    // 设置超时时间3秒,内部未超时
    @Transactional(timeout = 2)
    public int insertTimeoutNotWork2(Student record) {
        return studentMapper.insert(record);
    }


    @Override
    // 设置超时时间3秒,内部超时
    @Transactional(propagation = Propagation.REQUIRES_NEW, timeout = 5)
    public int insertTimeoutRequireNew(Student record) {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
