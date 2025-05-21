package shiro2.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import shiro2.pojo.entity.ExamUserRole;

public interface ExamUserRoleMapper extends BaseMapper<ExamUserRole> {
    int insert(ExamUserRole record);
}