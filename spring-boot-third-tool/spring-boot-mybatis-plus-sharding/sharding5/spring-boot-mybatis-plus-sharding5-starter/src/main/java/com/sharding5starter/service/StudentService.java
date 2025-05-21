package com.sharding5starter.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sharding5starter.dao.sharding.StudentShardingMapper;
import com.sharding5starter.entity.Student;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;


@Service
public class StudentService extends ServiceImpl<StudentShardingMapper, Student> {

    /**
     * 重写 saveBatch, 必须指定事务管理器
     * 否则会报错: No qualifying bean of type 'PlatformTransactionManager' available:
     * expected single matching bean but found 2
     */
    @Override
    public boolean saveBatch(Collection<Student> entityList) {
        return saveBatch(entityList, DEFAULT_BATCH_SIZE);
    }

    public int insertList(List<Student> list){
        return getBaseMapper().insertList(list);
    }
}
