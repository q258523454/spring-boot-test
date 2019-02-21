package com.test.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * Created By
 *
 * @author :   zhangjian
 * @date :   2018-08-30
 */

// 拦截所有的/login请求
@WebFilter(urlPatterns = "/login/*", filterName = "loginFilter")
public class LoginFilter implements Filter {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡ 过滤器: Login filter init ≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡");

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡ 过滤器: Login filter doFilter ≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡");
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        String userName = request.getParameter("userName");
//        if ("admin".equals(userName)) {
        // doFilter就是放行的意思
        filterChain.doFilter(servletRequest, servletResponse);
//        } else {
//            // 跳转指定页面， 注意"/"表示tomcat根路径, 没有则是项目相对路径
//            // 静态问价默认查找顺序: META-INF/resources > resources > static > public
//            response.sendRedirect("login-error.html");
//            return;
//        }spring-boot-listener-filter-intercept-aop-test
    }

    @Override
    public void destroy() {
        log.info("≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡ 过滤器: Login filter destroy ≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡");
    }
}
