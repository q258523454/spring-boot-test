package com.example.use1_base.conditionabean;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

public class ConditionalConfig {

    @Bean
    @ConditionalOnBean(ConditionalBean1.class)
    public ConditionalBean2 bean2() {
        return new ConditionalBean2();
    }
}
