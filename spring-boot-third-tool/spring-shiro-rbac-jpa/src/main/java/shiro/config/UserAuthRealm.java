package shiro.config;

import shiro.dao.UserRepository;
import shiro.pojo.entity.Permission;
import shiro.pojo.entity.Role;
import shiro.pojo.entity.User;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 */
@Component
public class UserAuthRealm extends AuthorizingRealm {

    @Resource
    private UserRepository userRepository;

    /**
     * 指定 加密算法,散列次数
     */
    @Resource
    HashedCredentialsMatcher hashedCredentialsMatcher;

    /**
     * 设置 realm的 HashedCredentialsMatcher
     */
    @PostConstruct
    public void setHashedCredentialsMatcher() {
        this.setCredentialsMatcher(hashedCredentialsMatcher);
    }


    /**
     * 权限核心配置 根据数据库中的该用户 角色 和 权限
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        User user = (User) principals.getPrimaryPrincipal();
        for (Role role : user.getRoles()) {                                 //获取 角色
            authorizationInfo.addRole(role.getName());                      //添加 角色
            for (Permission permission : role.getPermissions()) {           //获取 权限
                authorizationInfo.addStringPermission(permission.getName());//添加 权限
            }
        }
        return authorizationInfo;
    }

    /**
     * 用户每次登录,调用
     * {@link org.apache.shiro.SecurityUtils}
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        User user = userRepository.findByUsername(username);
        if (user == null) return null;
        String credentials = user.getPasswordSalt() + user.getUsername() + user.getPasswordSalt();//自定义加盐 salt + username + salt
        return new SimpleAuthenticationInfo(
                user, //用户名
                user.getPassword(), //密码
                ByteSource.Util.bytes(credentials), //加密
                getName()  //realm name
        );
    }


}
