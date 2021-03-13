package com.oracle.batch.service;


import com.oracle.batch.entity.Student;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: zhangj
 * @Date: 2019-12-16
 * @Version 1.0
 */
public interface IOracleStudentService {
    List<Student> selectAll();

    List<Student> selectAllByIdList(List<String> list);

    List<Student> selectByName(Student student);

    Student selectByPrimaryKey(BigDecimal id);

    int insert(Student record);

    long getMaxId();

    int update(Student record);

    /**
     * 批量插入——单条SQL batch
     */
    int insertListBatch(List<Student> studentList);

    /**
     * 批量插入——多条SQL BEGIN END
     */
    int insertListBeginEnd(List<Student> studentList);

    int updateByName(Student updated, String name);

    int updateById(Student updated, BigDecimal id);

    /**
     * 批量更新——单条SQL batch
     */
    int updateListByIdBatch(List<Student> studentList);

    int updateListByIdBeginEnd(List<Student> studentList);


}
