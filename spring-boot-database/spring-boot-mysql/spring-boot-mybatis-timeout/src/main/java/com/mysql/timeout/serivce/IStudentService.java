package com.mysql.timeout.serivce;


import com.mysql.timeout.entity.Student;

import java.util.List;

/**
 * @Description
 * @date 2020-03-12 22:01
 * @modify
 */
public interface IStudentService {

    List<Student> selectAll();

    int selectMaxId();

    int insert(Student record);

    int insertTimeout(Student record);

    int insertTimeoutRequireNew(Student record);

    int insertTimeoutNotWork1(Student record);

    int insertTimeoutNotWork2(Student record);

    Student selectByPrimaryKey(Integer id);

    int updateByName(Student updated, String name);

    List<Student> selectByName(String name);
}
