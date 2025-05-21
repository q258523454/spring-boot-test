
package shiro2.serivce.impl;

import shiro2.pojo.entity.ExamPermission;
import shiro2.pojo.entity.ExamRole;
import shiro2.pojo.entity.ExamUser;
import shiro2.serivce.ExamPermissionService;
import shiro2.serivce.ExamRolePermissionService;
import shiro2.serivce.ExamRoleService;
import shiro2.serivce.ExamUserService;
import shiro2.serivce.MyService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MyServiceImpl implements MyService {

    @Autowired
    private ExamUserService examUserService;
    @Autowired
    private ExamRoleService examRoleService;
    @Autowired
    private ExamPermissionService examPermissionService;
    @Autowired
    private ExamRolePermissionService examRolePermissionService;


    /**
     * 获取角色信息集合
     *
     * @Return Map<String, Object> 返回结果
     */
    @Override
    @RequiresPermissions("sys:role:info")
    public Map<String, Object> getRoleInfoList() {
        Map<String, Object> map = new HashMap<>();
        List<ExamRole> roleList = examRoleService.list();
        map.put("sysRoleEntityList", roleList);
        return map;
    }


    @Override
    @RequiresPermissions("sys:info:all")
    public Map<String, Object> getInfoAll() {
        Map<String, Object> map = new HashMap<>();
        List<ExamUser> userList = examUserService.list();
        map.put("userList", userList);
        List<ExamRole> roleList = examRoleService.list();
        map.put("roleList", roleList);
        List<ExamPermission> permissionList = examPermissionService.list();
        map.put("permissionList", permissionList);
        return map;
    }
}

