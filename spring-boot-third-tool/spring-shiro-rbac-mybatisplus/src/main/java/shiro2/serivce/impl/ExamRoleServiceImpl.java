package shiro2.serivce.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import shiro2.dao.ExamRoleMapper;
import shiro2.pojo.entity.ExamRole;
import shiro2.serivce.ExamRoleService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamRoleServiceImpl extends ServiceImpl<ExamRoleMapper, ExamRole> implements ExamRoleService {
    @Override
    public List<ExamRole> selectRoleByUserId(Long userId) {
        return this.baseMapper.selectRoleByUserId(userId);
    }

}
