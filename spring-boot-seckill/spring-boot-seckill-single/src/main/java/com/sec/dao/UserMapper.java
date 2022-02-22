package com.sec.dao;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.sec.pojo.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKey(User record);

    int insertList(@Param("list")List<User> list);


}