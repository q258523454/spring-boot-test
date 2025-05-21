package com.example.use2_selector_bean.config;

import com.example.use2_selector_bean.entity.AopProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;




@Slf4j
@Configuration
@EnableConfigurationProperties(AopProperties.class)
@ConditionalOnProperty(prefix = "my2.aop", name = "auto", havingValue = "true", matchIfMissing = true)
public class SelectBeanConfig1 {
    {
        log.info("SelectBeanConfig1：模拟AOP配置");
    }

    /**
     * 这是模拟 springboot AopAutoConfiguration 的配置, jdk 和 cglib 二选一实例化
     * bean 实例化必须同时满足以下2个条件:
     * 1.my2.aop.auto 没有配置 或者 my2.aop.auto: true
     * 2.my2.aop.proxy 没有配置 或者 my2.aop.proxy: true
     */
    @Configuration
    @ConditionalOnProperty(prefix = "my2.aop", name = "proxy", havingValue = "true", matchIfMissing = true)
    public static class Cglib {
        {
            log.info("Cglib：静态内部类 AOP默认:Cglib");
        }
    }

    /**
     * bean 实例化必须同时满足以下2个条件:
     * 1.my2.aop.auto 没有配置 或者 my2.aop.auto: true
     * 2.my2.aop.proxy: false
     */
    @Configuration
    @ConditionalOnProperty(prefix = "my2.aop", name = "proxy", havingValue = "false", matchIfMissing = false)
    public static class Jdk {
        {
            log.info(" Jdk:静态内部类 AOP调整:Jdk");
        }
    }


}