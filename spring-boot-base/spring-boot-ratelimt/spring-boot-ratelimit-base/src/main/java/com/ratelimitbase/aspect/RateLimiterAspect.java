package com.ratelimitbase.aspect;

import com.ratelimitbase.config.EnvironmentConfig;
import com.ratelimitbase.enums.RateLimiterTypeEnum;
import com.ratelimitbase.exception.RateLimiterErrorCode;
import com.ratelimitbase.exception.RateLimiterException;
import com.ratelimitbase.strategy.AbstractRateLimiterStrategy;
import com.ratelimitbase.strategy.factory.RateLimiterStrategyFactory;
import com.ratelimitbase.utils.JoinPointUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.text.MessageFormat;
import java.util.Map;

/**
 * 限流处理切面类
 *
 * @author zz/z0zz
 * @since 2023-06-14 11:42
 */
@Aspect
@Component
@Order(1)
@Slf4j
public class RateLimiterAspect {

    @Autowired
    private EnvironmentConfig environmentConfig;

    private static final String DOT = ".";

    /**
     * key 优先使用配置里面的(为了支持动态修改)
     * 举例1:
     * 配置里面加:
     * com.xxx.TestService#test1(com.pojo.Student,java.lang.String).limit=100
     * com.xxx.TestService#test1(com.pojo.Student,java.lang.String).type=guava
     * 那么 TestService#test1 这个方法的限流策略就是limit=100,type=guava, 注解上的值将被忽略
     * 举例2:
     * 配置里面加:
     * com.xxx.TestService#test1(com.xxx.Student,java.lang.String).limit=5
     * com.xxx.TestService#test1(com.xxx.Student,java.lang.String).type=guava
     * com.xxx.TestService#test1(com.xxx.Student,java.lang.String).spel=#student.age,#student.id
     * com.xxx.TestService#test1(com.xxx.Student,java.lang.String).callBackMethod=callBack2
     * 那么 TestService#test1这个方法的限流策略替换成 limit=100,type=guava, spel={#student.age,#student.id},callBackMethod=callBack2
     */
    @Around("@annotation(rateLimiterAnnotationMethod)")
    public Object method(ProceedingJoinPoint pjp, RateLimiterAnnotation rateLimiterAnnotationMethod) throws Throwable {
        String fullyMethodPathWithParameter = JoinPointUtils.getFullyMethodPathWithParameter(pjp);
        String limitPropertyValue = environmentConfig.getPropertyValue(fullyMethodPathWithParameter + DOT + "limit", false);
        String typePropertyValue = environmentConfig.getPropertyValue(fullyMethodPathWithParameter + DOT + "type", false);
        // limit,type 两者都不为空, 则用配置文件中的限流
        if (!StringUtils.isEmpty(limitPropertyValue) && !StringUtils.isEmpty(typePropertyValue)) {
            changeRateLimiter(rateLimiterAnnotationMethod, fullyMethodPathWithParameter);
        }
        if (rateLimiterAnnotationMethod.limit() <= 0) {
            throw new IllegalArgumentException("Rate limit limit must > 0");
        }
        RateLimiterTypeEnum rateLimiterTypeEnum = rateLimiterAnnotationMethod.type();
        AbstractRateLimiterStrategy strategy = RateLimiterStrategyFactory.getRateLimiterByEnum(rateLimiterTypeEnum);
        if (ObjectUtils.isEmpty(strategy)) {
            log.error("rate limit type:[{}] not register", rateLimiterTypeEnum.getCode());
            Integer errorCode = RateLimiterErrorCode.RATE_LIMIT_TYPE_NOT_REGISTER.getCode();
            String errorMsg = MessageFormat.format(RateLimiterErrorCode.RATE_LIMIT_TYPE_NOT_REGISTER.getMessage(), rateLimiterTypeEnum.getCode());
            throw new RateLimiterException(errorCode, errorMsg);
        }
        // 限流key
        return strategy.handle(pjp, rateLimiterAnnotationMethod);
    }

    /**
     * 配置文件存在配置,则替换注解对应的值,实现动态策略
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void changeRateLimiter(RateLimiterAnnotation rateLimiterAnnotationMethod, String fullyMethodPathWithParameter) throws NoSuchFieldException, IllegalAccessException {
        if (StringUtils.isEmpty(fullyMethodPathWithParameter)) {
            throw new IllegalArgumentException("Rate limit [fullyMethodPathWithParameter] must not null");
        }
        // 获取 RateLimiterAnnotation 代理实例所持有的 InvocationHandler
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(rateLimiterAnnotationMethod);
        // 获取 AnnotationInvocationHandler 的 memberValues 字段
        Field annotationField = invocationHandler.getClass().getDeclaredField("memberValues");
        // private修饰，打开权限
        annotationField.setAccessible(true);
        // 获取 memberValues
        Map<String, Object> memberValues = null;
        if (annotationField.get(invocationHandler) instanceof Map) {
            memberValues = (Map) annotationField.get(invocationHandler);
        } else {
            throw new IllegalArgumentException(MessageFormat.format("Rate limit @Annotations reflect error, class:{0}", fullyMethodPathWithParameter));
        }
        String limitPropertyValue = environmentConfig.getPropertyValue(fullyMethodPathWithParameter + DOT + "limit");
        String typePropertyValue = environmentConfig.getPropertyValue(fullyMethodPathWithParameter + DOT + "type");
        String spelPropertyValue = environmentConfig.getPropertyValue(fullyMethodPathWithParameter + DOT + "spel", false);
        String callBackMethodPropertyValue = environmentConfig.getPropertyValue(fullyMethodPathWithParameter + DOT + "callBackMethod", false);

        String[] newSpel = StringUtils.isEmpty(spelPropertyValue) ? new String[]{""} : spelPropertyValue.split(",");
        RateLimiterTypeEnum newType = getRateLimiterTypeEnumByName(typePropertyValue);
        long newLimit = Long.parseLong(limitPropertyValue);
        String newCallBackMethod = StringUtils.isEmpty(callBackMethodPropertyValue) ? "" : callBackMethodPropertyValue;
        // 修改注解中的 value 属性值
        memberValues.put("spel", newSpel);
        memberValues.put("type", newType);
        memberValues.put("limit", newLimit);
        memberValues.put("callBackMethod", newCallBackMethod);
        // 修改之后的注解值,会一直存在于运行内存,后面每次读取的时候,都是改变后的值
        log.info("Rate limit read from self properties, method:[{}], after change: spel:[{}],type:[{}],limit:[{}],callBackMethod:[{}]",
                fullyMethodPathWithParameter, newSpel, newType.getCode(), newLimit, newCallBackMethod);
    }

    /**
     * 通过名字获取具体的 RateLimiterTypeEnum
     */
    private RateLimiterTypeEnum getRateLimiterTypeEnumByName(String rateLimiterTypeName) {
        if (StringUtils.isEmpty(rateLimiterTypeName)) {
            throw new IllegalArgumentException("Rate limiter type name can not be null");
        }
        RateLimiterTypeEnum rateLimiterTypeEnum = null;
        try {
            rateLimiterTypeEnum = RateLimiterTypeEnum.parse(rateLimiterTypeName);
        } catch (IllegalArgumentException ex) {
            log.error("Can not find rate limiter type enum, type:[{}]", rateLimiterTypeName);
            throw ex;
        }
        return rateLimiterTypeEnum;
    }
}
