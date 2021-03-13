package com.security.service;



import com.security.entity.User;

import java.util.List;

/**
 * Created By
 *
 * @date :   2018-11-05
 */
public interface UserService {
    public Integer insertUser(User user);

    public List<User> findAll();

    public User findByUserName(String username);

}
