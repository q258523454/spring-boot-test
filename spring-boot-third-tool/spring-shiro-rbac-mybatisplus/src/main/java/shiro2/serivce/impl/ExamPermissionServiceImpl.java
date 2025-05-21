package shiro2.serivce.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import shiro2.dao.ExamPermissionMapper;
import shiro2.pojo.entity.ExamPermission;
import shiro2.serivce.ExamPermissionService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamPermissionServiceImpl extends ServiceImpl<ExamPermissionMapper, ExamPermission> implements ExamPermissionService {
    @Override
    public List<ExamPermission> selectPermissionByRoleId(Integer roleId) {
        return this.baseMapper.selectPermissionByRoleId(roleId);
    }
}
