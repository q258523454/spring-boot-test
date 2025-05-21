package com.multidb.dao.masterslave;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.multidb.entity.StudentMasterSlave;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StudentMasterSlaveMapper extends BaseMapper<StudentMasterSlave> {
    int insertList(@Param("list") List<StudentMasterSlave> list);

    StudentMasterSlave selectById(@Param("id") Integer id);
}