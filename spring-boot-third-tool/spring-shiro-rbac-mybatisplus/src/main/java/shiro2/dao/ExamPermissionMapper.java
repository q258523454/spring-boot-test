package shiro2.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import shiro2.pojo.entity.ExamPermission;

import java.util.List;

public interface ExamPermissionMapper extends BaseMapper<ExamPermission> {
    int insert(ExamPermission record);

    List<ExamPermission> selectPermissionByRoleId(Integer roleId);
}