package com.security.controller;

import com.security.constant.CONSTANT;
import com.security.dao.UserMapper;
import com.security.entity.User;
import com.security.exception.PasswordException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class LoginController {

    @Autowired
    private UserMapper userMapper;

    /**
     * 自定义生成Token，因为使用了自定义登录，就不会执行JWTLoginFilter了，所以需要字段调用生成token并返给前端
     */
    @RequestMapping(value = "/login")
    public void login(User user, HttpServletResponse response) {
        User userOut = userMapper.findByUserName(user.getUsername());
        if (userOut != null) {
            String pwdIn = user.getPassword();
            if (!pwdIn.equals(userOut.getPassword())) {
                throw new PasswordException("账号或密码错误.");
            }
            // 存放角色list集合,默认为空
            List<String> roleList = new ArrayList<>();
            String subject = userOut.getUsername() + "-" + roleList;
            String token = Jwts.builder()
                    .setSubject(subject)
                    // 设置过期时间 10 * 60 秒
                    .setExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                    .signWith(SignatureAlgorithm.HS512, CONSTANT.SIGN_KEY) // 算法自选
                    .compact();
            // 登录成功后，返回token到header里面
            response.addHeader("MyAuthorization", "MyToken " + token);
        }
    }
}
