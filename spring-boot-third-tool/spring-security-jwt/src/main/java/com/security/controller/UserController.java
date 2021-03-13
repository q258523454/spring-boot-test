package com.security.controller;

import com.alibaba.fastjson.JSONObject;
import com.security.entity.User;
import com.security.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created By
 *
 * @date :   2018-11-05
 */
@RestController
public class UserController {


    protected Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping("/security")
    public String security() {
        return "hello security";
    }

    // com.springBootTest.security.SecurityConfig 中已经将改url置为了白名单, 不进行拦截
    @GetMapping(value = "/test")
    public String test() {
        return "不需要验证.";
    }

    @GetMapping(value = "/insertUser")
    public String insertUser() {
        User user = new User();
        user.setUsername("abc");
        String str = bCryptPasswordEncoder.encode("123");
        user.setPassword(str);
        return userService.insertUser(user).toString();
    }

    /**
     * 获取用户列表
     */
    @GetMapping("/userList")
    public String userList() {
        return JSONObject.toJSONString(userService.findAll());
    }

}

