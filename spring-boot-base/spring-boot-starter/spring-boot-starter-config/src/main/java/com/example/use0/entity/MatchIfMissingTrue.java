package com.example.use0.entity;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * matchIfMissing = true (可以为空, 配置项为空时, 由 spring.factories默认实例化)
 * 实例化：
 * 1.当配置项为空:  如果有配置 spring.factories, 则会以默认属性值实例化
 * 2.当配置项不为空: 不管 spring.factories 是否配置, 属性值必须等于 havingValue(默认为''), 才会实例化。 否则不会实例化
 */
@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "match-if-missing-true")
@ConditionalOnProperty(prefix = "match-if-missing-true", value = "open", havingValue = "true", matchIfMissing = true)
public class MatchIfMissingTrue {
    private String zhang = "MatchIfMissingTrue";
    private boolean open;
}
