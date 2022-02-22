package com.batch.dao;

import java.util.List;

import com.batch.entity.Student3;
import org.apache.ibatis.annotations.Param;

public interface Student3Mapper {
    int deleteByPrimaryKey(Long id);

    int insert(Student3 record);

    Long getMaxId();

    int insertOrUpdate(Student3 record);

    int insertOrUpdateSelective(Student3 record);

    int insertSelective(Student3 record);

    Student3 selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Student3 record);

    int updateByPrimaryKey(Student3 record);

    int updateBatch(List<Student3> list);

    int updateBatchSelective(List<Student3> list);

    int batchInsert(@Param("list") List<Student3> list);
}