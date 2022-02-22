package com.aop.service.impl;

import com.aop.dao.OracleStudentMapper;
import com.aop.entity.Student;
import com.aop.service.IOracleStudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * @Author: zhangj
 * @Date: 2019-12-16
 * @Version 1.0
 */

@Service
public class OracleStudentServiceImpl implements IOracleStudentService {
    private static final Logger logger = LoggerFactory.getLogger(OracleStudentServiceImpl.class);

    @Autowired
    protected OracleStudentMapper studentMapper;

    @Override
    public int insert(Student record) {
        return studentMapper.insert(record);
    }

    @Override
    public long getMaxId() {
        return studentMapper.getMaxId();
    }

    @Override
    public List<Student> selectAll() {
        return studentMapper.selectAll();
    }

    @Override
    public int insertListBatch(List<Student> studentList) {
        return studentMapper.insertListBatch(studentList);
    }

    @Override
    public int update(Student record) {
        return studentMapper.update(record);
    }

    @Override
    public int updateByName(Student updated, String name) {
        return studentMapper.updateByName(updated, name);
    }

    @Override
    @Transactional
    public int updateRequire(Student record) {
        return studentMapper.update(record);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int updateRequireNew(Student record) {
        return studentMapper.update(record);
    }

    @Override
    public int updateListByIdBatchOrg(List<Student> studentList) {
        return studentMapper.updateListByIdBatchOrg(studentList);
    }

    @Override
    public int updateListByIdBatchAop(List<Student> studentList) {
        return studentMapper.updateListByIdBatchAop(studentList);
    }

    @Override
    @Transactional
    public int updateListByIdBatchTransAop(List<Student> studentList) {
        Student student = getStudent(null);
        studentMapper.insert(student);
        return studentMapper.updateListByIdBatchAop(studentList);
    }

    @Override
    public int updateListByIdBatchAopHaveTrans(List<Student> studentList) {
        return studentMapper.updateListByIdBatchAopHaveTrans(studentList);
    }

    @Override
    public int updateListByIdBatchAopHaveTransSelf(List<Student> studentList) {
        return studentMapper.updateListByIdBatchAopHaveTransSelf(studentList);
    }

    /**
     * 默认就是 Propagation.REQUIRED, AOP里面为编程式事务, Propagation.REQUIRED_NEW
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int updateListByIdBatchTransAopHaveTransSelf(List<Student> studentList) {
        Student student = getStudent(null);
        studentMapper.insert(student);
        try {
            return studentMapper.updateListByIdBatchAopHaveTransSelf(studentList);
        } catch (Exception ex) {
            logger.error("AOP SQL执行失败,全部回滚，不影响业务层事务");
            return 0;
        }
    }

    @Override
    @Transactional
    public int updateListByIdBatchTransCatchExceptionAop(List<Student> studentList) {
        try {
            return studentMapper.updateListByIdBatchAop(studentList);
        } catch (Exception ex) {
            logger.error("出现了异常:" + ex.getMessage());
            return 0;
        }
    }

    public Student getStudent(String id) {
        String wId = "";
        if (null != id && !id.isEmpty()) {
            wId = id;
        } else {
            wId = String.valueOf(studentMapper.getMaxId() + 1);
        }
        Student student = new Student();
        student.setId(new BigDecimal(wId));
        student.setName(UUID.randomUUID().toString().substring(0, 3));
        student.setAge(new BigDecimal("20"));
        return student;
    }

}
