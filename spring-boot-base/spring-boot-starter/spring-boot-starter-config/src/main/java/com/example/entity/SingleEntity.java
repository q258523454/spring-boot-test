package com.example.entity;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "single")
// single属性不允许为空,必须为open:true才实例化成bean
@ConditionalOnProperty(prefix = "single", value = "open", havingValue = "true", matchIfMissing = false)
public class SingleEntity {
    private String zhang;
    private boolean open;
}
