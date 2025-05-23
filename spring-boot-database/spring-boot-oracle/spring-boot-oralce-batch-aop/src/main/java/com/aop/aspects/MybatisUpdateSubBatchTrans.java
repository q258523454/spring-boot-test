package com.myjetcache.aspects;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MybatisUpdateSubBatchTrans {

    /**
     * 执行行数
     */
    int num() default 500;

}
