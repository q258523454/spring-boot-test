package shiro2.serivce.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import shiro2.dao.ExamUserMapper;
import shiro2.pojo.entity.ExamUser;
import shiro2.serivce.ExamUserService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamUserServiceImpl extends ServiceImpl<ExamUserMapper, ExamUser> implements ExamUserService {
    @Override
    public List<ExamUser> selectUserByName(String username) {
        return this.baseMapper.selectByUsername(username);
    }
}
