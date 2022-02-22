package com.example.use1_base.conditionabean;


import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;


/**
 * {@link ConditionalOnBean}: 表示如果没有对应的bean则不会实例化
 * 实测:
 * spring.factories 如果没有配置启动类 {@link ConditionalBean1}, Bean2 不会注入
 * 只有Bean1 注入后, Bean2 才会注入
 */
@Component
@ConditionalOnBean(ConditionalBean1.class)
public class ConditionalBean2 {
    private String name;
}
