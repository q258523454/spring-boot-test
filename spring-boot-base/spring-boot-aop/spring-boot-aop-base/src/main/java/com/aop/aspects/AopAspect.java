package com.aop.aspects;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created By
 *
 * @date :   2018-08-29
 */
@Aspect
@Component
@Slf4j
public class AopAspect {

    /**
     * 常用写法:
     * execution([返回参数] [包路径].[类].[方法](参数))
     * 包路径内 *代表所有, .. 表示当前目录及其子目录
     *
     * 例如:
     * 拦截所有 controller 包下面的,以-Controller为后缀的类, 方法返回参数为String的所有方法(入参可以为空)
     * 写法如下:
     * @Pointcut("execution(java.lang.String *..controller..*Controller.*(..))")
     * 踩坑,注意下面这种写法的入参不能为空.要特别注意。
     * @Pointcut("execution(java.lang.String *..controller..*Controller.*(*))")
     *
     *
     * 其他写法:
     * @Poincut("@annotation(myLogger)") 只拦截有注解为@myLogger的方法
     * @Poincut("within(com.service.*) ") 只拦截com.service包下的方法
     * @Poincut("bean(*service)") 只拦截所有bean中以service结尾的bean中的方法
     *
     */
    @Pointcut("execution(java.lang.String *..controller..*Controller.*(..))")
    public void controller() {

    }

    /**
     * AOP : 在切面中所有切点之前记录http基本信息, JoinPoint 可以让我们获取切点类名和方法
     * JoinPoint: 切点, joinPoint.getSignature().getDeclaringTypeName(): 切点的具体类名(全限定)
     * {@link Before}           前置通知
     * {@link After}            后置通知
     * {@link AfterReturning}   返回通知
     * {@link AfterThrowing}    异常通知
     * -----------------------------------------------------------
     * {@link Around}           环绕通知(核心),可实现上述4个注解的功能
     * try{
     *      try{
     *          do-Before();     // 对应{@link Before}注解的方法切面逻辑
     *          method.invoke();
     *      }finally{
     *          do-After();      // 对应{@link After}注解的方法切面逻辑
     *      }
     *      do-AfterReturning(); // 对应{@link AfterReturning}注解的方法切面逻辑
     *  }catch(Exception e){
     *       do-AfterThrowing(); // 对应{@link AfterThrowing}注解的方法切面逻辑
     *  }
     *
     *  实测正常情况:
     *  @Around
     *  @Before
     *  proceed()
     *  @Around
     *  @After(正常异常都会执行)
     *  @AfterReturning
     *
     *  异常情况:
     *  @Around
     *  @Before
     *  proceed()
     *  @After(正常异常都会执行)
     *  @AfterThrowing
     *
     */
    @Before("controller()")
    public void before(JoinPoint joinPoint) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
        log.info("@Before:URL = {} ", httpServletRequest.getRequestURI());
    }


    /**
     * 不管有无异常都会执行
     */
    @After(value = "controller()")
    public void after(JoinPoint joinPoint) {
        log.info("@After");
    }

    /**
     * 没有异常的时候执行
     */
    @AfterReturning(pointcut = "controller()", returning = "result")
    public void afterreturning(JoinPoint joinPoint, Object result) {
        log.info("@AfterReturning:{}", JSON.toJSONString(result));
    }

    @Around(value = "controller()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("@Around start");
        Object proceed = null;
        try {
            log.info("@Around before proceed()");
            proceed = joinPoint.proceed();
            // 注意这种写法可能存在问题,上面proceed()并不会执行
            log.info("@Around end proceed()");
            return proceed;
        } catch (Throwable throwable) {
            log.error("@Around - 切面出现异常");
            throw throwable;
        }
    }

    /**
     * 只有连接点抛出异常的时候才执行 {@link org.aspectj.lang.annotation.AfterThrowing}
     * @param joinPoint  连接点
     * @param ex 这里定义为 Exception, 表示通知所有异常.可以指定某个异常
     */
    @AfterThrowing(pointcut = "controller()", throwing = "ex")
    public void afterthrowing(JoinPoint joinPoint, Exception ex) {
        log.info("@AfterThrowing");
    }
}
