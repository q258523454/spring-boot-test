package com.batch.aspects;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MybatisUpdateSubBatchTransSelf {

    /**
     * 执行行数(默认500)
     */
    int num() default 500;

}
