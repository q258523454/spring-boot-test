package shiro2.util;


import shiro2.crazycake.shiro.RedisCacheManager;
import shiro2.crazycake.shiro.RedisManager;
import shiro2.crazycake.shiro.RedisSessionDAO;
import shiro2.pojo.entity.ExamUser;
import shiro2.shiro.ShiroRealm;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.LogoutAware;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;

/**
 * @Description Shiro工具类
 */

public class ShiroUtils {

    private static RedisSessionDAO redisSessionDAO = SpringUtil.getBean(RedisSessionDAO.class);

    /**
     * 获取当前用户Session
     *
     * @Return ExamUser 用户信息
     */
    public static Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }

    /**
     * 用户登出
     */
    public static void logout() {
        SecurityUtils.getSubject().logout();
    }

    /**
     * 获取当前用户信息
     *
     * @Return ExamUser 用户信息
     */
    public static ExamUser getUserInfo() {
        return (ExamUser) SecurityUtils.getSubject().getPrincipal();
    }

    /**
     * 删除用户缓存信息
     *
     * @Param username  用户名称
     * @Param isRemoveSession 是否删除Session
     * @Return void
     */
    public static void deleteCache(String username, boolean isRemoveSession) {
        // 从缓存中获取Session
        Session session = null;
        Collection<Session> sessions = redisSessionDAO.getActiveSessions();
        ExamUser examUser;
        Object attribute = null;
        for (Session sessionInfo : sessions) {
            // 遍历Session,找到该用户名称对应的Session
            attribute = sessionInfo.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if (attribute == null) {
                continue;
            }
            examUser = (ExamUser) ((SimplePrincipalCollection) attribute).getPrimaryPrincipal();
            if (examUser == null) {
                continue;
            }
            if (Objects.equals(examUser.getUsername(), username)) {
                session = sessionInfo;
                break;
            }
        }
        if (session == null) {
            return;
        }
        // 删除session
        if (isRemoveSession) {
            redisSessionDAO.delete(session);
        }
        // 删除Cache，在访问受限接口时会重新授权
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        Authenticator authc = securityManager.getAuthenticator();
        ((LogoutAware) authc).onLogout((SimplePrincipalCollection) attribute);
    }
}