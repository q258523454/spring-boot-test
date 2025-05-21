package shiro2.controller;

import shiro2.pojo.entity.ExamPermission;
import shiro2.pojo.entity.ExamRole;
import shiro2.pojo.entity.ExamRolePermission;
import shiro2.pojo.entity.ExamUser;
import shiro2.serivce.ExamPermissionService;
import shiro2.serivce.ExamRolePermissionService;
import shiro2.serivce.ExamRoleService;
import shiro2.serivce.ExamUserService;
import shiro2.serivce.MyService;
import shiro2.util.ShiroUtils;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 权限测试
 */
@RestController
@RequestMapping("/api/menu")
public class UserMenuController {

    @Autowired
    private ExamUserService examUserService;
    @Autowired
    private ExamRoleService examRoleService;
    @Autowired
    private ExamPermissionService examPermissionService;
    @Autowired
    private ExamRolePermissionService examRolePermissionService;

    @Autowired
    private MyService myService;

    /**
     * 获取用户信息集合
     */
    @RequestMapping("/getUserInfoList")
    @RequiresPermissions("sys:user:info")
    public Map<String, Object> getUserInfoList() {
        Map<String, Object> map = new HashMap<>();
        List<ExamUser> userList = examUserService.list();
        map.put("sysUserEntityList", userList);
        return map;
    }

    // /**
    //  * 获取角色信息集合
    //  *
    //  * @Return Map<String, Object> 返回结果
    //  */
    // @RequestMapping("/getRoleInfoList")
    // @RequiresPermissions("sys:role:info")
    // public Map<String, Object> getRoleInfoList() {
    //     Map<String, Object> map = new HashMap<>();
    //     List<ExamRole> roleList = examRoleService.list();
    //     map.put("sysRoleEntityList", roleList);
    //     return map;
    // }

    /**
     * 获取角色信息集合
     *
     * @Return Map<String, Object> 返回结果
     */
    @RequestMapping("/getRoleInfoList")
    public Map<String, Object> getRoleInfoList() {
        return myService.getRoleInfoList();
    }

    /**
     * 获取所有数据
     *
     * @Return Map<String, Object>
     */
    @RequestMapping("/getInfoAll")
    public Map<String, Object> getInfoAll() {
        return myService.getInfoAll();
    }

    /**
     * 获取权限信息集合
     */
    @RequestMapping("/getMenuInfoList")
    @RequiresPermissions(value = "sys:menu:info")
    public Map<String, Object> getMenuInfoList() {
        Map<String, Object> map = new HashMap<>();
        List<ExamPermission> permissionList = examPermissionService.list();
        map.put("permissionList", permissionList);
        return map;
    }

    // /**
    //  * 获取所有数据
    //  *
    //  * @Return Map<String, Object>
    //  */
    // @RequestMapping("/getInfoAll")
    // @RequiresPermissions("sys:info:all")
    // public Map<String, Object> getInfoAll() {
    //     Map<String, Object> map = new HashMap<>();
    //     List<ExamUser> userList = examUserService.list();
    //     map.put("userList", userList);
    //     List<ExamRole> roleList = examRoleService.list();
    //     map.put("roleList", roleList);
    //     List<ExamPermission> permissionList = examPermissionService.list();
    //     map.put("permissionList", permissionList);
    //     return map;
    // }

    /**
     * 添加管理员角色权限(测试动态权限更新)
     */
    @RequestMapping("/addMenu")
    public Map<String, Object> addMenu() {
        // 添加管理员角色权限
        ExamRolePermission rolePermission = new ExamRolePermission();
        rolePermission.setRoleId(1);
        rolePermission.setPermissionId(4);
        rolePermission.setIsEnable(1);
        rolePermission.setCreatedAt(new Date());
        rolePermission.setUpdatedAt(new Date());
        examRolePermissionService.save(rolePermission);
        // 清除缓存
        String username = "admin";
        ShiroUtils.deleteCache(username, false);
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("msg", "权限添加成功");
        return map;
    }
}