package com.ratelimitbase.aspect;


import com.ratelimitbase.enums.RateLimiterTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 限流方法
 *
 * @author zz/z0zz
 * @since 2023-06-14 11:42
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimiterAnnotation {

    /**
     * SpEL表达式, 动态指定限流key后缀
     * 注意:如果指定spel表达式,则要保证spel={"#object.param"}的值执行结果不为空
     * 否则视为 SpEL 没有填写, 那么就无法按照 SpEL 值进行限流, 仍然还是按接口维度限流
     */
    String[] spel() default "";

    /**
     * 限流算法,默认 guava
     * 优先 typeName(),其次 type()
     */
    RateLimiterTypeEnum type() default RateLimiterTypeEnum.GUAVA;

    /**
     * 限流次数
     */
    long limit() default -1;

    /**
     * 限流后降级处理方法,通过反射获取
     * 1.必须与限流方法在同个类下定义;
     * 2.入参必须与限流接口保持一致;
     */
    String callBackMethod() default "";

}
