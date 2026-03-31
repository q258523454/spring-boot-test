package com.sec.service.impl;

import com.sec.pojo.entity.User;

import java.util.List;

public interface UserService {


    int insert(User record);

    int insertSelective(User record);

    int updateByPrimaryKey(User record);

    User selectByPrimaryKey(Long id);


    int insertList(List<User> list);

}

