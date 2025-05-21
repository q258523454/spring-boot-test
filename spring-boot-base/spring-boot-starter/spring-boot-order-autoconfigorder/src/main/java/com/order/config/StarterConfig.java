package com.order.config;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * matchIfMissing = false 的时候,配置 spring.factories 是无效的.
 * 如果想要启动被实例化,必须有该配置项,且属性值必须等于 havingValue(默认为'')
 * 以下两个情况都不会实例化:
 * 1.无该配置项; (注意:即使配置了 spring.factories, 也不会实例化)
 * 2.有配置该属性,但属性值不等于 havingValue;
 */
@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "starter")
@ConditionalOnProperty(prefix = "starter", value = "open", havingValue = "true", matchIfMissing = false)
public class StarterConfig {
    private String msg;
    private boolean open;

    public StarterConfig() {
        log.info("------------------ 外部依赖Bean, 开始实例化 StarterConfig --------------");
    }
}
