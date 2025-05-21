package com.ratelimitbase.utils;

import com.ratelimitbase.aspect.RateLimiterAnnotation;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.expression.EvaluationContext;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public enum RateLimiterKeyUtils {
    ;

    /**
     * 资源分割符
     */
    private static final String SEPARATOR = "-";

    /**
     * 构造限流key
     * SpEL为空的时候 key=全限定方法路径(带参数列表)
     * 例如: com.xxx.TestService#test1(com.pojo.Student,java.lang.String)
     *
     * SpEL不空的时候 key=全限定方法路径(带参数列表)+"-"+SpEL表达式值
     * 例如:com.xxx.TestService#test1(com.pojo.Student,java.lang.String)-123
     */
    public static String createRateLimiterKey(ProceedingJoinPoint pjp, RateLimiterAnnotation rateLimiterAnnotationMethod) throws NoSuchMethodException {
        // 获取全限定方法路径(带参数列表)
        String fullyMethodPathWithParameter = JoinPointUtils.getFullyMethodPathWithParameter(pjp);
        // 将方法的参数名和参数值绑定
        String spelValues = getSpelValues(pjp, rateLimiterAnnotationMethod);
        // 限流key = 全限定方法路径(带参数列表)
        String rateLimiterKey = fullyMethodPathWithParameter;
        if (!ObjectUtils.isEmpty(spelValues)) {
            // 如果 SpEL表达式值 不为空, 则拼接到后缀
            rateLimiterKey += spelValues;
        }
        if (StringUtils.isEmpty(rateLimiterKey)) {
            throw new IllegalArgumentException("Create rate limiter key error.");
        }
        // 默认限流key值: 类全限定名称+方法+参数列表
        return rateLimiterKey;
    }

    /**
     * 计算SpEL的值,用"-"拼接
     */
    private static String getSpelValues(ProceedingJoinPoint pjp, RateLimiterAnnotation rateLimiterAnnotationMethod) throws NoSuchMethodException {
        if (ObjectUtils.isEmpty(rateLimiterAnnotationMethod.spel())) {
            return null;
        }
        List<String> spelList = Arrays.asList(rateLimiterAnnotationMethod.spel());
        if (spelList.size() == 1 || StringUtils.isEmpty(spelList.get(0))) {
            return null;
        }
        // 获取方法的参数值
        Object[] args = pjp.getArgs();
        Method method = JoinPointUtils.getMethod(pjp);
        EvaluationContext context = SpelUtils.getEvaluationContextWithBindParam(method, args);
        StringBuilder spelStringBuilder = new StringBuilder();
        List<Object> valueBySpelList = SpelUtils.getValueBySpelList(spelList, context);
        for (Object spelValue : valueBySpelList) {
            spelStringBuilder.append(SEPARATOR).append(spelValue.toString());
        }
        return spelStringBuilder.toString();
    }
}
