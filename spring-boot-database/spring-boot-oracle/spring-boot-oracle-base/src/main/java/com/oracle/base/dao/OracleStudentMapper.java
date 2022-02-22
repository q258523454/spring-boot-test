package com.oracle.base.dao;


import com.oracle.base.entity.Student;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;


@Repository
public interface OracleStudentMapper {
    List<Student> selectAll();

    List<Student> selectByName(@Param("updated") Student updated);

    long getMaxId();

    Student selectByPrimaryKey(BigDecimal id);

    int insert(Student record);

    /**
     * 批量插入——单条SQL foreach
     */
    int insertListBatch(@Param("studentList") List<Student> studentList);


    /**
     * 批量插入——多条SQL BEGIN END
     */
    int insertListBeginEnd(@Param("studentList") List<Student> studentList);


    int update(Student record);

    int updateByName(@Param("updated") Student updated, @Param("name") String name);

    int updateById(@Param("updated") Student updated, @Param("id") BigDecimal name);

    int updateListByIdBatch(@Param("studentList") List<Student> studentList);

    int updateListByIdBeginEnd(@Param("studentList") List<Student> studentList);
}