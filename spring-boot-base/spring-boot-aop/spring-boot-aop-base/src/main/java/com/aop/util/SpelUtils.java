package com.aop.util;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public enum SpelUtils {
    ;

    private static final ExpressionParser PARSER = new SpelExpressionParser();

    private static final LocalVariableTableParameterNameDiscoverer DISCOVERER = new LocalVariableTableParameterNameDiscoverer();


    /**
     * 将方法的参数名和参数值绑定
     *
     * @param method 方法，根据方法获取参数名
     * @param args   方法的参数值
     */
    public static EvaluationContext getEvaluationContextWithBindParam(Method method, Object[] args) {
        // 获取方法的参数名
        String[] parameterNames = DISCOVERER.getParameterNames(method);
        // 将参数名与参数值对应起来
        EvaluationContext context = new StandardEvaluationContext();
        if (null != parameterNames && parameterNames.length > 0) {
            for (int len = 0; len < parameterNames.length; len++) {
                context.setVariable(parameterNames[len], args[len]);
            }
        }
        return context;
    }


    /**
     * 根据 spel 表达式获取值
     *
     * @param spel    spel
     * @param context context
     */
    public static Object getValueBySpel(String spel, EvaluationContext context) {
        Expression expression = PARSER.parseExpression(spel);
        return expression.getValue(context);
    }

    /**
     * 根据 spelList 表达式获取值
     *
     * @param spelList spelList
     * @param context  context
     */
    public static List<Object> getValueBySpelList(List<String> spelList, EvaluationContext context) {
        List<Object> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(spelList)) {
            for (String spel : spelList) {
                Object valueBySpel = getValueBySpel(spel, context);
                result.add(valueBySpel);
            }
        }
        return result;
    }
}
