package shiro2.controller;


import shiro2.serivce.ExamPermissionService;
import shiro2.serivce.ExamRolePermissionService;
import shiro2.serivce.ExamRoleService;
import shiro2.serivce.ExamUserService;
import shiro2.util.ShiroUtils;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 角色测试
 */
@RestController
@RequestMapping("/api/role")
public class UserRoleController {

    @Autowired
    private ExamUserService examUserService;
    @Autowired
    private ExamRoleService examRoleService;
    @Autowired
    private ExamPermissionService examPermissionService;
    @Autowired
    private ExamRolePermissionService examRolePermissionService;

    /**
     * 管理员角色测试接口
     */
    @RequestMapping("/getAdminInfo")
    @RequiresRoles("admin")
    public Map<String, Object> getAdminInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("msg", "这里是只有管理员角色能访问的接口");
        return map;
    }

    /**
     * 用户角色测试接口
     */
    @RequestMapping("/getUserInfo")
    @RequiresRoles("user")
    public Map<String, Object> getUserInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("msg", "这里是只有用户角色能访问的接口");
        return map;
    }

    /**
     * 角色测试接口
     */
    @RequestMapping("/getRoleInfo")
    @RequiresRoles(value = {"admin", "user"}, logical = Logical.OR)
    @RequiresUser
    public Map<String, Object> getRoleInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("msg", "这里是只要有ADMIN或者USER角色能访问的接口");
        return map;
    }

    /**
     * 登出
     */
    @GetMapping("/getLogout")
    @RequiresUser
    public Map<String, Object> getLogout() {
        // 登出Shiro会帮我们清理掉Session和Cache
        ShiroUtils.logout();
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("msg", "登出");
        return map;
    }
}
