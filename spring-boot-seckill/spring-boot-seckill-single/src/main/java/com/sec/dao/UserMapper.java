package com.sec.dao;

import com.sec.pojo.entity.User;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKey(User record);

    int insertList(@Param("list") List<User> list);


}