package com.redis.service.impl;

import com.redis.dao.StudentMapper;
import com.redis.entity.Student;
import com.redis.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created By
 *
 * @author :   zhangjian
 * @date :   2018-08-28
 */

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public Student insertStudent(Student student) {
        Integer success = studentMapper.insertStudent(student);
        return success >= 1 ? student : null;
    }


    @Override
    public Integer deleteStundetById(int id) {
        return studentMapper.deleteStundetById(id);
    }

    @Override
    public Integer updateStudent(Student student) {
        return studentMapper.updateStudent(student);
    }

    @Override
    public Student selectStudentById(int id) {
        return studentMapper.selectStudentById(id);
    }

    @Override
    public List<Student> selectAllStuddent() {
        return studentMapper.selectAllStuddent();
    }

    @Override
    @Transactional
    public Integer transcationInsertStudent(Student student1, Student student2) {
        studentMapper.insertStudent(student1);
        studentMapper.insertStudent(student2);
        return 1;
    }
}
