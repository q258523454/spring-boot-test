package com.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.entity.Student;

import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Mapper 继承该接口后，无需编写 mapper.xml 文件，即可获得 CRUD 功能
 * 继承 BaseMapper 是基本的 CRUD, 针对的是 DAO 层.
 * 继承 IService 是加强版的 CRUD, 针对的是 Service 层, 含有更丰富 batch 操作.
 * 二者可以结合使用
 */
@Repository
public interface StudentPlusMapper extends BaseMapper<Student> {

    /**
     * 对应 mapper.xml
     */
    List<Student> selectAll();
}