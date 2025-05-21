package shiro2.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import shiro2.pojo.entity.ExamPermission;
import shiro2.pojo.entity.ExamRole;

import java.util.List;

public interface ExamRoleMapper extends BaseMapper<ExamRole> {
    int insert(ExamRole record);

    List<ExamRole> selectRoleByUserId(Long userId);
}