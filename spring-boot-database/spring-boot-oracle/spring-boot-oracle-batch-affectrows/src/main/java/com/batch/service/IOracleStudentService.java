package com.batch.service;


import com.batch.entity.Student;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;


public interface IOracleStudentService {
    List<Student> selectAll();

    List<Student> selectAllByIdList(List<String> list);

    List<Student> selectByName(Student student);

    Student selectByPrimaryKey(BigDecimal id);

    int insert(Student record);

    Long getMaxId();

    Long getMinId();

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

    // 单条件 update batch
    int updateListByIdBatch(List<Student> studentList);

    // 多条件 update batch (这里为了方便模拟,两个条件都是id,实际情况换成不同条件即可)
    int updateListByIdAndIdBatch(@Param("studentList") List<Student> studentList);


    int updateListByIdBeginEnd(List<Student> studentList);

    int deleteById(String id);

    int deleteByIdList(List<String> list);


}
