package com.multi.dao.mysql;


import com.multi.entity.mysql.Student;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MysqlStudentMapper {
    /**
     * @mbg.generated 2019-12-17
     */
    int insert(Student record);

    /**
     * @mbg.generated 2019-12-17
     */
    Student selectByPrimaryKey(Integer id);

    List<Student> selectAll();


}