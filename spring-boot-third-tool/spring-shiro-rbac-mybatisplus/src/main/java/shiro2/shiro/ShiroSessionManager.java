package shiro2.shiro;

import shiro2.config.ShiroRedisConfig;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 自定义获取Token
 */
public class ShiroSessionManager extends DefaultWebSessionManager {
    // 定义常量
    private static final String TOKEN_SESSION_ID_SOURCE = "Stateless request";

    // 重写构造器
    public ShiroSessionManager() {
        super();
        this.setDeleteInvalidSessions(true);
    }

    /**
     * 重写方法实现从请求头获取Token便于接口统一
     * 每次请求进来,Shiro会去从请求头找Authorization这个key对应的Value(Token)
     */
    @Override
    public Serializable getSessionId(ServletRequest request, ServletResponse response) {
        // 自定义实现, 从 Header 中的 Authorization 获取 token
        String token = WebUtils.toHttp(request).getHeader(ShiroRedisConfig.SESSION_NAME);
        if (!StringUtils.isEmpty(token)) {
            // 参考默认实现 org.apache.shiro.web.session.mgt.DefaultWebSessionManager.getReferencedSessionId
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, TOKEN_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, token);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return token;
        }
        // 如果 Header 中的 Authorization 为空, 则按默认规则从 Cookie 取 Token(JSESSIONID), 也可以自定义实现
        // DefaultWebSessionManager.getSessionId(request, response)
        return super.getSessionId(request, response);
    }
}