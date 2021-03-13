package com.trans.service;

import com.trans.entity.Teacher;

import java.util.List;

/**
 * Created By
 *
 * @author :   zhangj
 * @date :   2019-02-22
 */
public interface TeacherService {
    List<Teacher> selectAllTeacher();

    int insertTeacher(Teacher teacher);
}
