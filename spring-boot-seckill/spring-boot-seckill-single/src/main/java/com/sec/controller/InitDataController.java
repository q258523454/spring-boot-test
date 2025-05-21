package com.sec.controller;

import com.sec.pojo.entity.User;
import com.sec.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class InitDataController {
    @Autowired
    private UserService userService;


    /**
     * 插入5000个user,进行测试
     */
    @GetMapping(value = "/init/user")
    public String initUser() {
        List<User> userList = new ArrayList<>();
        // initial 5000 users
        for (int i = 1; i <= 5000; i++) {
            User user = createUser(i);
            userList.add(user);
        }
        userService.insertList(userList);
        return "ok";
    }

    /**
     * 创建一个user
     */
    private User createUser(long id) {
        User user = new User();
        user.setId(id);
        user.setNickname("");
        // 初始密码 123456
        user.setPassword("b7797cce01b4b131b433b6acf4add449");
        user.setSalt("");
        user.setHead("");
        user.setRegisterDate(LocalDateTime.now());
        user.setLastLoginDate(LocalDateTime.now());
        user.setLoginCount(0);
        return user;
    }
}
