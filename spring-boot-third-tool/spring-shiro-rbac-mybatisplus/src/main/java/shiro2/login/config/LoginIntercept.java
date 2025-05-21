package shiro2.login.config;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import shiro2.pojo.entity.ExamUser;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
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

        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            ExamUser examUser = (ExamUser) SecurityUtils.getSubject().getPrincipal();
            log.info("--------------- 拦截器: Shiro 已登录:{} ---------------", JSONObject.toJSONString(examUser));
        } else {
            log.warn("--------------- 拦截器: Shiro 未登录 ---------------");
        }
        // 验证身份和登陆
        Subject subject2 = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("张三", "123456");
        // 进行登录操作
        subject2.login(token);
        return true;
    }

    /***
     * 进入controller方法之后, 如果Controller出现异常, 不调用该方法
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }

    /***
     * controller结束, 无论Controller是不是有异常, 都执行该方法, 一般用于清理资源, 统一异常处理、统一日志处理
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }


}
