package com.aop.aspects;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MybatisUpdateSubBatch {

    /**
     * 执行行数
     */
    int num() default 500;

}
