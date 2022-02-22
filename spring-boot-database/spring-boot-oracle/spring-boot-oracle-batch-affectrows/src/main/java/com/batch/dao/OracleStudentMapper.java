package com.batch.dao;


import com.batch.entity.Student;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;


@Repository
public interface OracleStudentMapper {
    List<Student> selectAll();

    List<Student> selectAllByIdList(@Param("list") List<String> list);

    List<Student> selectByName(@Param("updated") Student updated);

    Long getMaxId();

    Long getMinId();

    Student selectByPrimaryKey(BigDecimal id);

    int insert(Student record);

    int update(Student record);

    int updateByName(@Param("updated") Student updated, @Param("name") String name);

    int updateById(@Param("updated") Student updated, @Param("id") BigDecimal id);

    int insertListBatch(@Param("studentList") List<Student> studentList);

    int insertListBeginEnd(@Param("studentList") List<Student> studentList);

    // 单条件 update batch
    int updateListByIdBatch(@Param("studentList") List<Student> studentList);

    // 多条件 update batch (这里为了方便模拟,两个条件都是id,实际情况换成不同条件即可)
    int updateListByIdAndIdBatch(@Param("studentList") List<Student> studentList);

    int updateListByIdBeginEnd(@Param("studentList") List<Student> studentList);

    int deleteById(@Param("id") String id);

    int deleteByIdList(@Param("list") List<String> list);

}