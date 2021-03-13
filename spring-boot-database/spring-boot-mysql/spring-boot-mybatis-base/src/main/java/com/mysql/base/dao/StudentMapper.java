package com.mysql.base.dao;

import com.mysql.base.entity.Student;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StudentMapper {
    /**
     * @mbg.generated 2019-12-17
     */
    int insert(Student record);


    /**
     * @mbg.generated 2019-12-17
     */
    Student selectByPrimaryKey(Integer id);

    List<Student> selectAll();

    int selectMaxId();

    int updateByName(@Param("updated") Student updated, @Param("name") String name);

    List<Student> selectByName(@Param("name") String name);
}