
package com.aop.aspects;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MySpel {
    /**
     * spel表达式 单个value
     */
    String spelSingleValue() default "";

    /**
     * spel表达式 数组
     */
    String[] spelArray() default "";

    /**
     * spel表达式 单个value
     */
    String spelObjValue() default "";
}