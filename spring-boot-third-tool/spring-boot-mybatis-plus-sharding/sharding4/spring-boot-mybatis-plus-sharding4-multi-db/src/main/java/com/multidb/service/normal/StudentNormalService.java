package com.multidb.service.normal;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.multidb.dao.normal.StudentNormalMapper;
import com.multidb.entity.StudentBase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;


@Service
public class StudentNormalService extends ServiceImpl<StudentNormalMapper, StudentBase> {

    /**
     * 重写 saveBatch, 必须指定事务管理器
     * 否则会报错: No qualifying bean of type 'PlatformTransactionManager' available:
     * expected single matching bean but found 2
     */
    @Transactional(value = "myNormalTransactionManager", rollbackFor = Exception.class)
    public boolean saveBatch(Collection<StudentBase> entityList) {
        return saveBatch(entityList, DEFAULT_BATCH_SIZE);
    }

    @Transactional(value = "myNormalTransactionManager", rollbackFor = Exception.class)
    public int insertList(List<StudentBase> list){
        return getBaseMapper().insertList(list);
    }

}
