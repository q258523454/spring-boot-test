package shiro2.serivce.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import shiro2.dao.ExamUserRoleMapper;
import shiro2.pojo.entity.ExamUserRole;
import shiro2.serivce.ExamUserRoleService;

import org.springframework.stereotype.Service;

@Service
public class ExamUserRoleServiceImpl extends ServiceImpl<ExamUserRoleMapper, ExamUserRole> implements ExamUserRoleService {
}
