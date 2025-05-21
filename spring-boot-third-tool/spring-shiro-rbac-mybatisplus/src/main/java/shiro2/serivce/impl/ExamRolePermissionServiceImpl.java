package shiro2.serivce.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import shiro2.dao.ExamRolePermissionMapper;
import shiro2.pojo.entity.ExamRolePermission;
import shiro2.serivce.ExamRolePermissionService;

import org.springframework.stereotype.Service;

@Service
public class ExamRolePermissionServiceImpl extends ServiceImpl<ExamRolePermissionMapper, ExamRolePermission> implements ExamRolePermissionService {
}
