package com.example.use3_selector_bean2.entity;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties(prefix = "my3")
public class MyProperties {
    /**
     * auto
     */
    private boolean auto;

    /**
     * 是否注入所有bean
     */
    private boolean useBean;
}
