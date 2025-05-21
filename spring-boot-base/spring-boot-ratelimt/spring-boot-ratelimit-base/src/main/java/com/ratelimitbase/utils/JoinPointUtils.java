package com.ratelimitbase.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 限流key值
 *
 * @author zz/z0zz
 * @since 2023-06-14 11:42
 */
public enum JoinPointUtils {
    ;

    /**
     * 获取全限定方法路径(带参数列表)
     * key示例: "com.xxx.TestService#test1(com.pojo.Student,java.lang.String)"
     */
    public static String getFullyMethodPathWithParameter(JoinPoint joinPoint) {
        StringBuilder sb = new StringBuilder();
        String fullClassName = getFullClassName(joinPoint);
        sb.append(fullClassName);
        String methodParameterName = getMethodParameterName(joinPoint);
        sb.append(methodParameterName);
        return sb.toString();
    }

    /**
     * 获取 JoinPoint 当前执行的方法
     */
    public static Method getMethod(ProceedingJoinPoint pjp) throws NoSuchMethodException {
        if (!(pjp.getSignature() instanceof MethodSignature)) {
            throw new IllegalArgumentException("Rate limit mush be used in method.");
        }
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        return pjp.getTarget().getClass().getMethod(method.getName(), method.getParameterTypes());
    }


    /**
     * 获取 joinPoint 指定方法名的Method对象
     */
    public static Method getMethodFromJoinPoint(JoinPoint joinPoint, String targetMethodName) {
        Method targetMethod = null;
        if (joinPoint.getSignature() instanceof MethodSignature) {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method currentMethod = signature.getMethod();
            // 注意指定方法和当前方法参数需要一致,且处于同一个类
            targetMethod = AopUtils.getDeclaredMethod(joinPoint.getTarget().getClass(), targetMethodName, currentMethod.getParameterTypes());
        }
        return targetMethod;
    }

    /**
     * 获取切面入参值(切点必须是方法上)
     */
    public static Map<String, Object> getNameValueMap(ProceedingJoinPoint proceedingJoinPoint) {
        if (!(proceedingJoinPoint.getSignature() instanceof MethodSignature)) {
            throw new IllegalArgumentException("JoinPoint is not method.");
        }
        Map<String, Object> map = new HashMap<>();
        Object[] values = proceedingJoinPoint.getArgs();
        String[] names = ((CodeSignature) proceedingJoinPoint.getSignature()).getParameterNames();
        for (int i = 0; i < names.length; i++) {
            map.put(names[i], values[i]);
        }
        return map;
    }

    /**
     * 获取 joinPoint 对应的全限定类名
     */
    private static String getFullClassName(JoinPoint joinPoint) {
        // 下面效果同 joinPoint.getTarget().getClass().getName()
        if (joinPoint.getSourceLocation() != null) {
            return joinPoint.getSourceLocation().getWithinType().getName();
        } else {
            return joinPoint.getSignature().getDeclaringType().getName();
        }
    }

    /**
     * 获取拼接方法名和参数名
     * eg: #test1(com.pojo.Student,java.lang.String)
     */
    private static String getMethodParameterName(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        if (!(signature instanceof MethodSignature)) {
            throw new IllegalArgumentException("JoinPoint is not method.");
        }
        MethodSignature methodSignature = (MethodSignature) signature;
        return AopUtils.getMethodPathWithParameter(methodSignature.getMethod());
    }
}
