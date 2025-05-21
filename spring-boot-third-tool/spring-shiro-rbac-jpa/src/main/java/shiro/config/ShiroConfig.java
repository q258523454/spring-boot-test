package shiro.config;

import shiro.dao.PermissionRepository;
import shiro.pojo.entity.Permission;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Resource
    private PermissionRepository permissionRepository;

    @Resource
    private UserAuthRealm userAuthRealm;

    /**
     * 配置 资源访问策略 . web应用程序 shiro核心过滤器配置
     */
    @Bean
    public ShiroFilterFactoryBean factoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);
        // 登录页
        factoryBean.setLoginUrl("/login");
        // 首页
        factoryBean.setSuccessUrl("/index");
        // 未授权界面
        factoryBean.setUnauthorizedUrl("/unauthorized");
        // 配置 拦截过滤器链
        factoryBean.setFilterChainDefinitionMap(setFilterChainDefinitionMap());
        return factoryBean;
    }

    /**
     * 拦截过滤器链.
     * map<url,Shiro权限>
     */
    private Map<String, String> setFilterChainDefinitionMap() {
        Map<String, String> filterMap = new LinkedHashMap<>();
        // 注册 数据库中所有的权限 及其对应url
        List<Permission> allPermission = permissionRepository.findAll();//数据库中查询所有权限
        for (Permission p : allPermission) {
            filterMap.put(p.getUrl(), "perms[" + p.getName() + "]");    //拦截器中注册所有的权限
        }
        // anon: 无需认证即可访问
        // authc: 需要认证才可访问
        // user: 点击“记住我”功能可访问
        // role: 拥有某个角色权限才能访问
        // perms: 拥有权限才可以访问
        filterMap.put("/static/**", "anon");    // 公开访问的资源
        filterMap.put("/open/api/**", "anon");  // 公开接口地址
        filterMap.put("/logout", "logout");     // 配置登出页,shiro已经帮我们实现了跳转
        filterMap.put("/**", "authc");          // 所有资源都需要经过验证
        return filterMap;
    }


    /**
     * 开启shiro 注解支持. 使以下注解能够生效 :
     * 需要认证 {@link org.apache.shiro.authz.annotation.RequiresAuthentication RequiresAuthentication}
     * 需要用户 {@link org.apache.shiro.authz.annotation.RequiresUser RequiresUser}
     * 需要访客 {@link org.apache.shiro.authz.annotation.RequiresGuest RequiresGuest}
     * 需要角色 {@link org.apache.shiro.authz.annotation.RequiresRoles RequiresRoles}
     * 需要权限 {@link org.apache.shiro.authz.annotation.RequiresPermissions RequiresPermissions}
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }


    /**
     * 配置 SecurityManager,可配置一个或多个realm
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userAuthRealm);
        return securityManager;
    }

    /**
     * 凭证匹配 : 指定 加密算法,散列次数
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");   //散列算法:这里使用MD5算法
        hashedCredentialsMatcher.setHashIterations(1024); //散列的次数，比如散列两次，相当于 md5(md5(""))
        return hashedCredentialsMatcher;
    }
}
