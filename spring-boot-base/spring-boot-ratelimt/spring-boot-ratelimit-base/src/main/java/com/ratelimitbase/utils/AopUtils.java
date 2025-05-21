package com.ratelimitbase.utils;

import java.lang.reflect.Method;

/**
 * AOP工具类
 *
 * @author zz/z0zz
 * @since 2023-06-14 11:42
 */
public class AopUtils {

    /**
     * 反射获取指定方法
     */
    public static Method getDeclaredMethod(Class<?> type, String methodName, Class<?>... parameterTypes) {
        Method method = null;
        try {
            method = type.getDeclaredMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            Class<?> superclass = type.getSuperclass();
            if (superclass != null) {
                method = getDeclaredMethod(superclass, methodName, parameterTypes);
            }
        }
        return method;
    }

    /**
     * 获取class下指定方法的全限定名称(带参数), 用于本地获取, 方便生产环境修改配置
     * 示例: "com.xxx.TestService#test1(com.pojo.Student,java.lang.String)"
     * 这个结果与 JoinPointUtils#getFullyMethodPathWithParameter 一致
     */
    public static String getFullyMethodPathWithParameter(Class<?> cls, String methodName) {
        String classFullName = "";
        String methodPathWithParameter = "";
        Method[] methods = cls.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                // 获取全限定类名
                classFullName = method.getDeclaringClass().getName();
                // 拼接方法名和参数名
                methodPathWithParameter = getMethodPathWithParameter(method);
                break;
            }
        }
        return classFullName + methodPathWithParameter;
    }

    /**
     * 获取拼接方法名和参数名
     * eg: #test1(com.pojo.Student,java.lang.String)
     */
    public static String getMethodPathWithParameter(Method method) {
        StringBuilder sb = new StringBuilder();
        sb.append("#");
        sb.append(method.getName());
        sb.append("(");
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int size = parameterTypes.length, i = 0; i < size; i++) {
            appendParamTypeName(sb, parameterTypes[i]);
            if (i < size - 1) {
                sb.append(",");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * 构造 StringBuilder
     * 递归遍历参数存在数组的情况
     */
    private static void appendParamTypeName(StringBuilder sb, Class<?> paramType) {
        if (paramType.isArray()) {
            appendParamTypeName(sb, paramType.getComponentType());
            sb.append("[]");
        } else {
            sb.append(paramType.getName());
        }
    }
}
