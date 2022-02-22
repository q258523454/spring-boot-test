package com.multi.dao.oracle;


import com.multi.entity.oracle.Student;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;


@Repository
public interface OracleStudentMapper {
    /**
     * @mbg.generated 2019-12-16
     */
    Student selectByPrimaryKey(BigDecimal id);

    List<Student> selectAll();

    int insert(Student record);

}