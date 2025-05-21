package com.myjetcache.dao;


import com.myjetcache.aspects.MybatisUpdateSubBatch;
import com.myjetcache.aspects.MybatisUpdateSubBatchTrans;
import com.myjetcache.aspects.MybatisUpdateSubBatchTransSelf;
import com.myjetcache.entity.Student;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;


@Repository
public interface OracleStudentMapper {

    long getMaxId();

    Student selectByPrimaryKey(BigDecimal id);

    List<Student> selectAll();

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

    int updateListByIdBatchOrg(@Param("studentList") List<Student> studentList);

    @MybatisUpdateSubBatch
    int updateListByIdBatchAop(@Param("studentList") List<Student> studentList);

    /**
     * 注解声明式事务 —— 对AOP不生效
     */
    @MybatisUpdateSubBatchTrans
    int updateListByIdBatchAopHaveTrans(@Param("studentList") List<Student> studentList);

    /**
     * 手动编程式事务
     * num是batch每次划分的条数
     */
    @MybatisUpdateSubBatchTransSelf(num = 5)
    int updateListByIdBatchAopHaveTransSelf(@Param("studentList") List<Student> studentList);


    int updateListByIdBeginEnd(@Param("studentList") List<Student> studentList);

    List<Student> findByAgeAndIdBetweenOrEqualToAndName2(@Param("age") BigDecimal age, @Param("minId") BigDecimal minId, @Param("maxId") BigDecimal maxId, @Param("name2") String name2);


}