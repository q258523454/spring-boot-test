package com.test.aspect;

import com.alibaba.fastjson.JSONObject;
import com.test.entity.Student;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created By
 *
 * @author :   zhangjian
 * @date :   2018-08-29
 */

@Aspect
@Component
public class AopTest {

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    // 针对Controller_Aop所有方法, 都定义为切点
    @Pointcut("execution(public * com.test.contoller.Controller_Aop.*(..))")
    public void myPointCut() {

    }

    // AOP : 在切面中所有切点之前记录http基本信息, JoinPoint 可以让我们获取切点类名和方法
    @Before("myPointCut()")
    // JoinPoint: 切点, joinPoint.getSignature().getDeclaringTypeName(): 切点的具体类名
    public void logHttpBeforeJoinPoint(JoinPoint joinPoint) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
        log.info("URL = { " + httpServletRequest.getRequestURI() + " }");
        log.info("method = { " + httpServletRequest.getMethod() + " }");
        log.info("ip = { " + httpServletRequest.getRemoteAddr() + " }");
        log.info("class.method = { " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName() + " }");
        log.info("args = { " + JSONObject.toJSONString(joinPoint.getArgs()) + " }");
    }

    // AOP: 获取切点的返回信息, 并打印
    @AfterReturning(returning = "object", pointcut = "myPointCut()")
    public void logHttpAfterJoinPoint(Object object) {
        log.info(JSONObject.toJSONString(object));
    }


    @Before("execution(public * com.test.contoller.Controller_Aop.aopTest(..))")
    public void beforeLog() {
        log.info("beforeLog");
    }

    @After("execution(public * com.test.contoller.Controller_Aop.aopAfter(..))")
    public void afterLog() {
        log.info("afterLog");
    }

    @After("execution(public * com.test.contoller.Controller_Aop.doInsertStudentAfterAop(..))")
    public void aopAfterInsertStudent() {
        Student student = new Student();
        student.setName("doInsertStudentAfterAop");
        student.setAge(Integer.toString(18 + (int) (Math.random() * (100 - 18) + 1)));
        log.info("aopAfterInsertStudent: " + JSONObject.toJSONString(student));
    }

}
