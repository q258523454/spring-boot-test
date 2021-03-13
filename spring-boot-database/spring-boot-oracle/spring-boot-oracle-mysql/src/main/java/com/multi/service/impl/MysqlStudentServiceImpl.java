package com.multi.service.impl;

import com.multi.dao.mysql.MysqlStudentMapper;
import com.multi.entity.mysql.Student;
import com.multi.service.IStudentServiceMysql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: zhangj
 * @Date: 2019-12-16
 * @Version 1.0
 */

@Service
public class MysqlStudentServiceImpl implements IStudentServiceMysql {
    private static final Logger logger = LoggerFactory.getLogger(MysqlStudentServiceImpl.class);

    @Autowired
    protected MysqlStudentMapper mysqlMapper;

    @Override
    public List<Student> selectAll() {
        return mysqlMapper.selectAll();
    }

    @Override
    public int insert(Student record) {
        return mysqlMapper.insert(record);
    }

}
