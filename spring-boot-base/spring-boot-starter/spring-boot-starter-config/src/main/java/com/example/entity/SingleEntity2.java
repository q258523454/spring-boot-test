package com.example.entity;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "single2")
// 该starter,要求必须配置 single2.open 属性为 true
@ConditionalOnProperty(prefix = "single2", value = "open", havingValue = "true", matchIfMissing = false)
public class SingleEntity2 {
    private String zhang = "default-zhang";
    private boolean open;
}
