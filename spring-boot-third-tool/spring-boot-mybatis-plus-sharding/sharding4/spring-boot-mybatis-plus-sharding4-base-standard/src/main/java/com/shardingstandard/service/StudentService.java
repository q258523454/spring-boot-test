package com.shardingstandard.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shardingstandard.dao.StudentPlusMapper;
import com.shardingstandard.entity.Student;

import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Mapper 继承该接口后，无需编写 mapper.xml 文件，即可获得 CRUD 功能
 * 继承 BaseMapper 是基本的 CRUD, 针对的是 DAO 层.
 * 继承 IService 是加强版的 CRUD, 针对的是 Service 层, 含有更丰富 batch 操作.
 * 二者结合使用
 * 多表操作或复杂SQL, 还是需要去写 mapper.xml
 */
@Service
public class StudentService extends ServiceImpl<StudentPlusMapper, Student> {

    public int insertList(List<Student> list) {
        return this.getBaseMapper().insertList(list);
    }
}
