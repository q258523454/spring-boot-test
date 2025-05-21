package com.annotation.annotation;


import com.annotation.config.AsyncThreadPoolConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({AsyncThreadPoolConfig.class})
public @interface MyAnnotaion {
    /**
     * @Import
     * 1: 直接声明bean
     * 2: 导入@Configuration配置类 (可实现多配置导入)
     * 其他: 导入 @ImportSelector 和  @ImportBeanDefinitionRgistrar
     *
     * @Configuration
     * 相当于xml配置bean
     *
     *
     */
}
