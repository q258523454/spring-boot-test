package com.security.service.impl;

import com.security.entity.User;
import com.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userOut = userService.findByUserName(username);
        if (null == userOut) {
            throw new UsernameNotFoundException(username + "账户不存在");
        }
        // 返回userdetails.User对象
        return new org.springframework.security.core.userdetails.User(userOut.getUsername(), userOut.getPassword(), emptyList());
    }
}
