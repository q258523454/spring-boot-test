package com.trans.service.impl;

import com.trans.dao.StudentMapper;
import com.trans.entity.Student;
import com.trans.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created By
 *
 * @author :   zhangj
 * @date :   2019-02-22
 */

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
