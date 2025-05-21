package com.multidb.service.masterslave;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.multidb.dao.masterslave.StudentMasterSlaveMapper;
import com.multidb.entity.StudentMasterSlave;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;


@Service
public class StudentMasterSlaveService extends ServiceImpl<StudentMasterSlaveMapper, StudentMasterSlave> {

    @Transactional(value = "myMasterSlaveTransactionManager", rollbackFor = Exception.class)
    public StudentMasterSlave selectById(Integer id) {
        return getBaseMapper().selectById(id);
    }

    @Override
    @Transactional(value = "myMasterSlaveTransactionManager", rollbackFor = Exception.class)
    public boolean saveBatch(Collection<StudentMasterSlave> entityList) {
        return saveBatch(entityList, DEFAULT_BATCH_SIZE);
    }

    @Transactional(value = "myMasterSlaveTransactionManager", rollbackFor = Exception.class)
    public int insertList(List<StudentMasterSlave> list) {
        return getBaseMapper().insertList(list);
    }


}
