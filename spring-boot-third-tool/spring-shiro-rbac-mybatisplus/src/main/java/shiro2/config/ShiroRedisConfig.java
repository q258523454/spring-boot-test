package shiro2.config;


import shiro2.crazycake.shiro.IRedisManager;
import shiro2.crazycake.shiro.RedisCacheManager;
import shiro2.crazycake.shiro.RedisClusterManager;
import shiro2.crazycake.shiro.RedisManager;
import shiro2.crazycake.shiro.RedisSessionDAO;
import shiro2.shiro.ShiroSessionIdGenerator;
import shiro2.shiro.ShiroSessionManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Configuration
public class ShiroRedisConfig {

    private static final String CACHE_KEY = "shiro:cache:";
    private static final String SESSION_KEY = "shiro:session:";

    /**
     * 默认使用 session.getTimeout() 单位 秒
     */
    private static final int LOGIN_SESSION_EXPIRE = -2;

    /**
     * 权限cache缓存时间, 单位 秒
     */
    private static final int PERMS_CACHE_EXPIRE = 300;


    /**
     * session 过期时间, 单位 秒 (全局)
     */
    private static final int SESSION_EXPIRE = 1800;


    /**
     * 单位 秒
     */
    private static final int SESSION_INTERVAL = 10;


    /**
     * 自定义 Cookie jsessionid 名字
     */
    public static final String SESSION_NAME = "ZJ_JSESSIONID";

    // Redis配置
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.timeout}")
    private int timeout;
    @Value("${spring.redis.password}")
    private String password;

    /**
     * 集群配置
     */
    @Value("#{'${spring.redis.cluster.nodes:}'.split(',')}")
    private List<String> nodes;

    /**
     * 配置Redis管理器, 集群使用 RedisClusterManager
     */
    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(port);
        redisManager.setTimeout(timeout);
        // redis密码为空 暂时不设置
        redisManager.setPassword(password);
        return redisManager;
    }


    @Bean
    public RedisClusterManager redisClusterManager() {
        if (!CollectionUtils.isEmpty(nodes) && nodes.size() > 1) {
            RedisClusterManager redisClusterManager = new RedisClusterManager();
            String join = StringUtils.join(nodes.toArray(), ",");
            redisClusterManager.setHost(join);
            redisClusterManager.setTimeout(timeout);
            // redis密码为空 暂时不设置
            // redisManager.setPassword(password);
            return redisClusterManager;
        }
        return null;
    }

    /**
     * 配置Cache管理器
     * 用于往Redis存储权限和角色标识
     */
    @Bean
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        IRedisManager redisManager = getRedisManager();
        redisCacheManager.setRedisManager(redisManager);
        redisCacheManager.setKeyPrefix(CACHE_KEY);
        // 配置缓存的话要求放在session里面的实体类必须有个id标识
        redisCacheManager.setPrincipalIdFieldName("userId");
        // 权限过期时间, 10s
        redisCacheManager.setExpire(PERMS_CACHE_EXPIRE);
        return redisCacheManager;
    }

    /**
     * SessionID生成器
     */
    @Bean
    public ShiroSessionIdGenerator sessionIdGenerator() {
        return new ShiroSessionIdGenerator();
    }

    /**
     * 配置 RedisSessionDAO
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        IRedisManager redisManager = getRedisManager();
        redisSessionDAO.setRedisManager(redisManager);
        redisSessionDAO.setSessionIdGenerator(sessionIdGenerator());
        redisSessionDAO.setKeyPrefix(SESSION_KEY);
        // 默认-2, 与 session timeout 保持一致 (会被全局 SessionTimeout 覆盖)
        redisSessionDAO.setExpire(LOGIN_SESSION_EXPIRE);
        return redisSessionDAO;
    }

    /**
     * 配置Session管理器
     */
    @Bean
    public SessionManager sessionManager() {
        ShiroSessionManager shiroSessionManager = new ShiroSessionManager();
        shiroSessionManager.setSessionDAO(redisSessionDAO());
        // session 过期时间 (毫秒ms)
        shiroSessionManager.setGlobalSessionTimeout(SESSION_EXPIRE * 1000);
        // 定时清理失效会话 shiroSessionManager.setSessionValidationInterval(SESSION_EXPIRE * 1000);
        // 删除过期的session,默认是true
        shiroSessionManager.setDeleteInvalidSessions(true);
        shiroSessionManager.setSessionValidationSchedulerEnabled(true);
        //  自定义 JSESSIONID 名字
        shiroSessionManager.setSessionIdCookie(new SimpleCookie(SESSION_NAME));
        return shiroSessionManager;
    }

    /**
     * 优先使用集群配置
     *
     * @return RedisCacheManager
     */
    private IRedisManager getRedisManager() {
        RedisClusterManager redisClusterManager = redisClusterManager();
        RedisManager redisManager = redisManager();
        // 优先使用集群配置
        if (null != redisClusterManager) {
            return redisClusterManager;
        } else {
            return redisManager;
        }
    }
}
