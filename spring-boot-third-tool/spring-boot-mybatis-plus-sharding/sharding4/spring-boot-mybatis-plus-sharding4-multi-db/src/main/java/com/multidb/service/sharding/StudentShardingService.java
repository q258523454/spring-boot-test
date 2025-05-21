package com.multidb.service.sharding;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.multidb.dao.sharding.StudentShardingMapper;
import com.multidb.entity.Student;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;


@Service
public class StudentShardingService extends ServiceImpl<StudentShardingMapper, Student> {

    /**
     * 重写 saveBatch, 必须指定事务管理器
     * 否则会报错: No qualifying bean of type 'PlatformTransactionManager' available:
     * expected single matching bean but found 2
     */
    @Override
    @Transactional(value = "myShardingTransactionManager", rollbackFor = Exception.class)
    public boolean saveBatch(Collection<Student> entityList) {
        return saveBatch(entityList, DEFAULT_BATCH_SIZE);
    }

    @Transactional(value = "myShardingTransactionManager", rollbackFor = Exception.class)
    public int insertList(List<Student> list){
        return getBaseMapper().insertList(list);
    }
}
