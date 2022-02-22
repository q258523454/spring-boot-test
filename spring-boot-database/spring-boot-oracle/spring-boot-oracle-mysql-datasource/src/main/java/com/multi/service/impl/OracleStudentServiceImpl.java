package com.multi.service.impl;

import com.multi.entity.oracle.Student;
import com.multi.dao.oracle.OracleStudentMapper;
import com.multi.service.IStudentServiceOracle;
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
public class OracleStudentServiceImpl implements IStudentServiceOracle {
    private static final Logger logger = LoggerFactory.getLogger(OracleStudentServiceImpl.class);

    @Autowired
    protected OracleStudentMapper oracleMapper;

    @Override
    public List<Student> selectAll() {
        return oracleMapper.selectAll();
    }

    @Override
    public int insert(Student record) {
        return oracleMapper.insert(record);
    }

//    @Override
//    public List<com.multi.entity.mysql.Student> selectAll_mysql() {
//        return mysqlMapper.selectAll();
//    }
//
//    @Override
//    public int insert_mysql(com.multi.entity.mysql.Student record) {
//        return mysqlMapper.insert(record);
//    }

}
