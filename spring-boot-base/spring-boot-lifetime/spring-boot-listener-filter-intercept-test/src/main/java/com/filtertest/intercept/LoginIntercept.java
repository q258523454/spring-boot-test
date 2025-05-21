package com.filtertest.intercept;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created By
 *
 * @date :   2018-08-31
 */

@Slf4j
public class LoginIntercept implements HandlerInterceptor {

    /***
     * Handler执行前调用
     * 应用场景：登录认证、身份授权
     * 返回值为true则是放行，为false是不放行
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("--------------- 拦截器: MyIntercept preHandle() ---------------");
        // 获取指定的参数数据，用于校验使用
        String userName = request.getParameter("userName");
        // 获取请求的URI
        String requestURI = request.getRequestURI();
        log.info("requestURI is:" + requestURI);
        if ("admin".equals(userName)) {
            log.info("userName is correct.");
        } else {
            log.info("userName is error.");
            // 不放行
            return false;
        }
        return true;
    }

    /***
     * 进入controller方法之后, 如果Controller出现异常, 不调用该方法
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        log.info("--------------- 拦截器: MyIntercept postHandle() ---------------");
    }

    /***
     * controller结束, 无论Controller是不是有异常, 都执行该方法, 一般用于清理资源, 统一异常处理、统一日志处理
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        log.info("--------------- 拦截器: MyIntercept afterCompletion() ---------------");
    }


}
