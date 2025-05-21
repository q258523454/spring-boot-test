package shiro2.controller;


import shiro2.serivce.MyService;
import shiro2.shiro.ShiroRealm;
import shiro2.util.ShiroUtils;
import shiro2.util.SpringContextHolder;

import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 用户登录
 */
@RestController
public class TestController {
    @Autowired
    private MyService myService;

    @GetMapping("/test")
    public String getAdminInfo() {
        return "ok";
    }

    @GetMapping("/a/test")
    public String getAdminInfo2() {
        return "a";
    }

    @GetMapping("/remove")
    public String remove(@Param("userId") Long userId) {
        SpringContextHolder.getBean(ShiroRealm.class).clearAuthCache(userId);
        return "a";
    }

}