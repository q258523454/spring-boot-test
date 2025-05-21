package shiro2.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import shiro2.pojo.entity.ExamRolePermission;

public interface ExamRolePermissionMapper extends BaseMapper<ExamRolePermission> {
    int insert(ExamRolePermission record);
}