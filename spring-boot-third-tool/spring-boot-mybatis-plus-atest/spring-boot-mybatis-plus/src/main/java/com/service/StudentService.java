package com.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.entity.Student;

import java.util.List;

public interface StudentService extends IService<Student> {

    List<Student> selectAll();
}
