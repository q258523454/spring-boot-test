
package com.aop.aspects;

import com.alibaba.fastjson.JSON;
import com.aop.util.SpelUtils;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Aspect
@Component
public class SpelAspect {

    @Pointcut("@annotation(com.aop.aspects.MySpel)")
    public void pointcut() {
    }

    /**
     * 注解值也可以通过 下面的方式获取值
     * MySpel mySpel = method.getAnnotation(MySpel.class);
     */
    @Around("pointcut()&&@annotation(mySpelObj)")
    public Object around(ProceedingJoinPoint pjp, MySpel mySpelObj) throws Throwable {

        log.info("limiter 参数:" + JSON.toJSONString(mySpelObj));

        // 获取方法的参数值
        Object[] args = pjp.getArgs();

        Method method = this.getMethod(pjp);

        // 将方法的参数名和参数值绑定
        EvaluationContext context = SpelUtils.getEvaluationContextWithBindParam(method, args);

        // 单个 spel
        Object spelSingleValue = SpelUtils.getValueBySpel(mySpelObj.spelSingleValue(), context);
        log.info("spel single value:" + spelSingleValue);

        // spel 数组
        List<Object> valueBySpelList = SpelUtils.getValueBySpelList(Arrays.asList(mySpelObj.spelArray()), context);
        log.info("spel list values:" + valueBySpelList);

        // spel 获取对象中value
        Object spelObjValue = SpelUtils.getValueBySpel(mySpelObj.spelObjValue(), context);
        log.info("spel object value:" + spelObjValue);

        return pjp.proceed();
    }

    /**
     * 获取当前执行的方法
     */
    private Method getMethod(ProceedingJoinPoint pjp) throws NoSuchMethodException {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        return pjp.getTarget().getClass().getMethod(method.getName(), method.getParameterTypes());
    }
}