package shiro2.shiro;

import shiro2.pojo.entity.ExamPermission;
import shiro2.pojo.entity.ExamRole;
import shiro2.pojo.entity.ExamUser;
import shiro2.serivce.ExamPermissionService;
import shiro2.serivce.ExamRoleService;
import shiro2.serivce.ExamUserService;
import shiro2.util.ShiroUtils;
import shiro2.util.SpringContextHolder;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description Shiro权限匹配和账号密码匹配
 */
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private ExamUserService examUserService;
    @Autowired
    private ExamRoleService examRoleService;
    @Autowired
    private ExamPermissionService examPermissionService;

    /**
     * @RequiresPermissions 会检查
     * {@link RequiresPermissions}
     * 授权权限, 先查redis. 每次登录后,会清空redis,重新查数据库
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 获取用户ID
        ExamUser examUser = (ExamUser) principalCollection.getPrimaryPrincipal();
        Long userId = examUser.getUserId();

        // 进行授权和处理
        Set<String> roleIdSet = new HashSet<>();
        Set<String> permissionSet = new HashSet<>();
        // 查询角色和权限
        List<ExamRole> examUserRoleList = examRoleService.selectRoleByUserId(userId);
        for (ExamRole examRole : examUserRoleList) {
            roleIdSet.add(examRole.getRoleName());
            List<ExamPermission> examPermissionList = examPermissionService.selectPermissionByRoleId(examRole.getId());
            for (ExamPermission examPermission : examPermissionList) {
                permissionSet.add(examPermission.getPermissionCode());
            }
        }
        // 将查到的权限和角色分别传入authorizationInfo中
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(roleIdSet);
        authorizationInfo.setStringPermissions(permissionSet);
        return authorizationInfo;
    }

    /**
     * 身份认证,用户每次登录,调用
     * SecurityUtils.getSubject().login()
     * 自定义实现shiro2.crazycake.shiro.RedisSessionDAO,会保存session到redis
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
        // 获取用户的输入的账号.
        String username = (String) authenticationToken.getPrincipal();
        // 通过username从数据库中查找 User对象，如果找到进行验证
        // 实际项目中,这里可以根据实际情况做缓存,如果不做,Shiro自己也是有时间间隔机制,2分钟内不会重复执行该方法
        List<ExamUser> examUsers = examUserService.selectUserByName(username);
        ExamUser user = examUsers.get(0);
        // 判断账号是否存在
        if (user == null) {
            throw new AuthenticationException();
        }
        // 判断账号是否被冻结
        if (user.getState() == null || !user.getState().equals("NORMAL")) {
            throw new LockedAccountException();
        }
        // 进行验证
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user,                                  // 用户名
                user.getPassword(),                    // 密码
                ByteSource.Util.bytes(user.getSalt()), // 设置盐值
                getName()
        );
        // 验证成功踢人(清除缓存和Session)
        // ShiroUtils.deleteCache(username, true);

        // // 将用户信息放入session中
        // Session session = SecurityUtils.getSubject().getSession();
        // // session中不需要保存密码
        // user.setPassword(null);
        // session.setAttribute("SESSION_USER_INFO", user);
        return authenticationInfo;
    }


    /**
     * 主动清空用户缓存,用于强制刷新
     */
    public void clearAuthCache(Long userId) {
        String cacheName = getAuthorizationCacheName();
        CacheManager cacheManager = SpringContextHolder.getBean(CacheManager.class);
        Cache<Object, Object> cache = cacheManager.getCache(cacheName);
        ExamUser examUser = new ExamUser();
        examUser.setUserId(userId);
        SimplePrincipalCollection simplePrincipalCollection = new SimplePrincipalCollection(examUser, getName());
        cache.remove(simplePrincipalCollection);
    }

    //
    // public void clearShiroCatch(String username) {
    //     Subject subject = SecurityUtils.getSubject();
    //     String realmName = subject.getPrincipals().getRealmNames().iterator().next();
    //     // 第一个参数为用户名,第二个参数为realmName,test想要操作权限的用户
    //     SimplePrincipalCollection principals = new SimplePrincipalCollection(username, realmName);
    //     subject.runAs(principals);
    //     ShiroRealm bean = SpringContextHolder.getBean(ShiroRealm.class);
    //     bean.getAuthorizationCache().remove(subject.getPrincipals());
    //     subject.releaseRunAs();
    // }


}
