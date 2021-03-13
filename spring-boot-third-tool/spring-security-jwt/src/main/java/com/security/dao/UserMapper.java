package com.security.dao;

import com.security.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created By
 *
 * @date :   2018-11-05
 */
@Repository
public interface UserMapper {

    public Integer insertUser(User user);

    public List<User> findAll();

    public User findByUserName(String username);

}
