package com.trans.service.impl;

import com.trans.dao.StudentMapper;
import com.trans.entity.Student;
import com.trans.service.StudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper mapper;

    @Override
    public List<Student> selectAllStudent() {
        return mapper.selectAllStudent();
    }

    @Override
    public int insertStudent(Student student) {
        return mapper.insertStudent(student);
    }

    @Override
    @Transactional
    public int insertStudentTrans(Student student) {
        return mapper.insertStudent(student);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int transactionRequiresNew() {
        Student student = Student.createStudent("student_transactionOrg");
        insertStudent(student);
        return 1;
    }

    @Override
    public List<Student> selectAllByUsername(String username) {
        return mapper.selectAllByUsername(username);
    }

    @Override
    public int updateById(Student updated, Long id) {
        return mapper.updateById(updated, id);
    }

    @Override
    public Student selectById(Long id) {
        return mapper.selectById(id);
    }

}
