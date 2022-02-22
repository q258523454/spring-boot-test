package com.sec.service.impl;
import java.util.List;

import com.sec.pojo.entity.User;

public interface UserService {


    int insert(User record);

    int insertSelective(User record);

    int updateByPrimaryKey(User record);

    User selectByPrimaryKey(Long id);



	int insertList(List<User> list);

}

