package com.example.use3_selector_bean2.config;

import com.example.use3_selector_bean2.entity.MyProperties;
import com.example.use3_selector_bean2.entity.SelectBean1;
import com.example.use3_selector_bean2.entity.SelectBean2;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;




@Slf4j
@Data
@Configuration
@EnableConfigurationProperties(MyProperties.class)
@ConditionalOnProperty(prefix = "my3", name = "auto", havingValue = "true", matchIfMissing = false)
public class SelectBeanConfig2 {

    public SelectBeanConfig2() {
        log.info("SelectBeanConfig2 Constructor");
    }

    /**
     * 同时注入
     * {@link SelectBeanConfig2}
     * {@link SelectBean1}
     * {@link SelectBean2}
     *
     * 必须有配置,且配置必须为:
     * my3.auto=true
     *
     */
    @Bean
    @ConditionalOnProperty(prefix = "my3", name = "auto", havingValue = "true", matchIfMissing = false)
    public SelectBean1 myBean1() {
        return new SelectBean1();
    }

    @Bean
    @ConditionalOnProperty(prefix = "my3", name = "auto", havingValue = "true", matchIfMissing = false)
    public SelectBean2 myBean2() {
        return new SelectBean2();
    }


}