package shiro2.controller;


import shiro2.pojo.entity.ExamUser;
import shiro2.pojo.entity.ExamUserRole;
import shiro2.serivce.ExamUserRoleService;
import shiro2.serivce.ExamUserService;
import shiro2.util.SHA256Util;
import shiro2.util.ShiroUtils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 用户登录
 */
@RestController
@RequestMapping("/login")
public class UserLoginController {

    @Autowired
    private ExamUserService examUserService;
    @Autowired
    private ExamUserRoleService examUserRoleService;

    /**
     * 登录
     */
    @RequestMapping("/in")
    public Map<String, Object> login(@RequestBody ExamUser examUser) {
        Map<String, Object> map = new HashMap<>();
        // 进行身份验证
        try {
            // 验证身份和登陆
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(examUser.getUsername(), examUser.getPassword());
            // 进行登录操作
            subject.login(token);
        } catch (IncorrectCredentialsException e) {
            map.put("code", 500);
            map.put("msg", "用户不存在或者密码错误");
            return map;
        } catch (LockedAccountException e) {
            map.put("code", 500);
            map.put("msg", "登录失败，该用户已被冻结");
            return map;
        } catch (AuthenticationException e) {
            map.put("code", 500);
            map.put("msg", "该用户不存在");
            return map;
        } catch (Exception e) {
            map.put("code", 500);
            map.put("msg", "未知异常");
            return map;
        }
        map.put("code", 0);
        map.put("msg", "登录成功");
        map.put("token", ShiroUtils.getSession().getId().toString());
        return map;
    }

    /**
     * 未登录
     */
    @RequestMapping("/unauth")
    public Map<String, Object> unauth() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 500);
        map.put("msg", "未登录");
        return map;
    }


    /**
     * 添加一个用户演示接口
     * 这里仅作为演示不加任何权限和重复查询校验
     */
    @RequestMapping("/add/user")
    public Map<String, Object> testAddUser() {
        // 设置基础参数
        ExamUser user = new ExamUser();
        user.setUsername("user1");
        user.setState("NORMAL");
        // 随机生成盐值
        String salt = RandomStringUtils.randomAlphanumeric(20);
        user.setSalt(salt);
        // 进行加密
        String password = "123456";
        user.setPassword(SHA256Util.sha256(password, user.getSalt()));
        // 保存用户
        examUserService.save(user);
        // 保存角色
        ExamUserRole examUserRole = new ExamUserRole();
        // 保存用户完之后会把ID返回给用户实体
        examUserRole.setUserId(Math.toIntExact(user.getUserId()));
        examUserRoleService.save(examUserRole);
        // 返回结果
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "添加成功");
        return map;
    }
}