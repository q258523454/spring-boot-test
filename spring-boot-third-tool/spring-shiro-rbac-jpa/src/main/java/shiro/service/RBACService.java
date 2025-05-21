package shiro.service;

import shiro.pojo.vo.Resp;

import org.springframework.http.ResponseEntity;

/**
 * RBAC  基于角色的权限访问控制系统 服务说明
 */
public interface RBACService {


    /**
     * 用户登陆服务
     * 1 . 传入用户名/密码
     * 2 . Shiro 登陆校验 ,加载数据库中用户的角色，权限。
     *
     * @param username 传入的用户名
     * @param password 传入的密码(明文)
     */
    ResponseEntity<Resp> login(String username, String password);

    ResponseEntity<Resp> logout();
}
