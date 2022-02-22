package com.example.use2_selector_bean.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "my2.aop")
public class AopProperties {

    /**
     * 是否自动装配
     */
    private boolean auto = true;
    /**
     * 是否按照原始方法初始化bean
     */
    private boolean proxy = true;

}
