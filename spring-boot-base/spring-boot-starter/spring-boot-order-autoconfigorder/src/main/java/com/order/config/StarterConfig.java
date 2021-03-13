package com.order.config;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "starter")
// 1.starter 属性允许为空(为空也实例化bean)
// 2.starter 非空时,必须为 open:true 才实例化成bean
@ConditionalOnProperty(prefix = "starter", value = "open", havingValue = "true", matchIfMissing = false)
public class StarterConfig {
    private String msg;
    private boolean open;

    public StarterConfig() {
        log.info("------------------ 外部依赖Bean, 开始实例化 StarterConfig --------------");
    }
}
