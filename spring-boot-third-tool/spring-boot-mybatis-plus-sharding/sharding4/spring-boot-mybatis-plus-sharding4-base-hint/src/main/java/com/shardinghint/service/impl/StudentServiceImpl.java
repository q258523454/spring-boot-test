package com.shardinghint.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shardinghint.service.StudentService;
import com.shardinghint.dao.StudentPlusMapper;
import com.shardinghint.entity.Student;

import org.springframework.stereotype.Service;


/**
 * Mapper 继承该接口后，无需编写 mapper.xml 文件，即可获得 CRUD 功能
 * 继承 BaseMapper 是基本的 CRUD, 针对的是 DAO 层.
 * 继承 IService 是加强版的 CRUD, 针对的是 Service 层, 含有更丰富 batch 操作.
 * 二者结合使用
 * 多表操作或复杂SQL, 还是需要去写 mapper.xml
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentPlusMapper, Student> implements StudentService {
}
