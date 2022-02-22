package com.sec.service;
import java.util.List;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.sec.dao.UserMapper;
import com.sec.pojo.entity.User;
import com.sec.service.impl.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public int insert(User record) {
        return userMapper.insert(record);
    }

    @Override
    public int insertSelective(User record) {
        return userMapper.insertSelective(record);
    }

    @Override
    public int updateByPrimaryKey(User record) {
        return userMapper.updateByPrimaryKey(record);
    }

    @Override
    public User selectByPrimaryKey(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

	@Override
	public int insertList(List<User> list){
		 return userMapper.insertList(list);
	}



}

