package com.test.intercept;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created By
 *
 * @author :   zhangjian
 * @date :   2018-08-31
 */

// Controller拦截器
public class MyIntercept2 implements HandlerInterceptor {

    private final Logger log = LoggerFactory.getLogger(MyIntercept2.class);

    /***
     * 进入controller方法之前
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("--------------- 拦截器: MyIntercept2 preHandle() ---------------");
        String userName = request.getParameter("userName");
        if ("admin".equals(userName)) {
            log.info("userName is correct.");
        } else {
            log.info("userName is error.");
        }
        return true;
    }

    /***
     * 进入controller方法之后, 如果Controller出现异常, 不调用该方法
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        log.info("--------------- 拦截器: MyIntercept2 postHandle() ---------------");
    }

    /***
     * controller结束, 无论Controller是不是有异常, 都执行该方法, 一般用于清理资源
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        log.info("--------------- 拦截器: MyIntercept2 afterCompletion() ---------------");
    }


}
