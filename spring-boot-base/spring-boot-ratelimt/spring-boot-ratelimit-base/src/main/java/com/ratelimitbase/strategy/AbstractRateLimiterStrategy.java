package com.ratelimitbase.strategy;


import com.ratelimitbase.aspect.RateLimiterAnnotation;
import com.ratelimitbase.exception.RateLimiterErrorCode;
import com.ratelimitbase.exception.RateLimiterException;
import com.ratelimitbase.utils.JoinPointUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.text.MessageFormat;

/**
 * 服务限流策略抽象类
 *
 * @author zz/z0zz
 * @since 2023-06-14 11:42
 */
@Slf4j
public abstract class AbstractRateLimiterStrategy {

    /**
     * 限流处理入口
     */
    public abstract Object handle(ProceedingJoinPoint pjp, RateLimiterAnnotation rateLimiterAnnotationMethod) throws Throwable;

    /**
     * 限流后的降级方法执行
     */
    protected Object callBackMethodExecute(String rateLimiterKey, ProceedingJoinPoint pjp, RateLimiterAnnotation rateLimiterAnnotationMethod) throws Exception {
        // 获取方法上的api注解名
        String apiName = getMethodApiName(pjp);
        log.error("Api:[{}],hit rate limit rule,limit:[{}],type:[{}]", apiName, rateLimiterAnnotationMethod.limit(), rateLimiterAnnotationMethod.type().getCode());
        if (StringUtils.isBlank(rateLimiterAnnotationMethod.callBackMethod())) {
            // 没有配置降级处理方法,则抛出异常
            log.error("Api:[{}] no callback method, be abandoned, args:{}", apiName, JoinPointUtils.getNameValueMap(pjp));
            Integer errorCode = RateLimiterErrorCode.HIT_RATE_LIMIT_ERROR.getCode();
            String errorMsg = MessageFormat.format(RateLimiterErrorCode.HIT_RATE_LIMIT_ERROR.getMessage(), rateLimiterKey);
            throw new RateLimiterException(errorCode, errorMsg);
        }
        Object obj = pjp.getTarget();
        Method method = JoinPointUtils.getMethodFromJoinPoint(pjp, rateLimiterAnnotationMethod.callBackMethod());
        if (method == null) {
            log.error("Callback method not exist,class:[{}],method:[{}]", obj.getClass().getName(), rateLimiterAnnotationMethod.callBackMethod());
            Integer errorCode = RateLimiterErrorCode.CALLBACK_METHOD_NOT_EXIST.getCode();
            String errorMsg = MessageFormat.format(RateLimiterErrorCode.CALLBACK_METHOD_NOT_EXIST.getMessage(), rateLimiterKey);
            throw new RateLimiterException(errorCode, errorMsg);
        }
        Object result = method.invoke(obj, pjp.getArgs());
        log.info("Api:[{}],execute callBack method,class:[{}],method:[{}]", apiName, obj.getClass().getName(), rateLimiterAnnotationMethod.callBackMethod());
        return result;
    }

    /**
     * 获取方法上的api注解名,不存在,则返回整个方法定义(全限定+修饰符+出入参)
     * 如果是Controller层,则返回请求path
     */
    private String getMethodApiName(ProceedingJoinPoint pjp) {
        String apiName = "";
        Signature signature = pjp.getSignature();
        if (!(signature instanceof MethodSignature)) {
            return apiName;
        }
        Method method = ((MethodSignature) signature).getMethod();
        if (null != method.getAnnotation(RequestMapping.class)) {
            apiName = method.getAnnotation(RequestMapping.class).value()[0];
        } else if (null != method.getAnnotation(GetMapping.class)) {
            apiName = method.getAnnotation(GetMapping.class).value()[0];
        } else if (null != method.getAnnotation(PostMapping.class)) {
            apiName = method.getAnnotation(PostMapping.class).value()[0];
        } else if (null != method.getAnnotation(PutMapping.class)) {
            apiName = method.getAnnotation(PutMapping.class).value()[0];
        } else if (null != method.getAnnotation(DeleteMapping.class)) {
            apiName = method.getAnnotation(DeleteMapping.class).value()[0];
        }
        if (StringUtils.isEmpty(apiName)) {
            // 如果api不存在,则用整个方法定义(全限定+修饰符+出入参)
            apiName = pjp.getSignature().toLongString();
        }
        return apiName;
    }
}
