package com.example.use0.entity;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Slf4j
/**
 * @ConditionalOnProperty 配置检查
 * matchIfMissing = false: 必须有该配置项和属性值,且必须等于havingValue, 否则不实例化, 默认是 false
 * matchIfMissing = true : 可以没有该配置项和属性值,但非空时候必须等于havingValue,否则不实例化
 * havingValue表示 当 prefix.value 相等的时候, 配置才会实例化  默认 havingValue = ""
 *
 *
 * matchIfMissing = false (spring.factories 不会实例化启动, 必须强制配置属性)
 * matchIfMissing = false 的时候,配置 spring.factories 是无效的.
 * 如果想要启动被实例化,必须有该配置项,且属性值必须等于 havingValue(默认为'')
 * 以下两个情况都不会实例化:
 * 1.无该配置项; (注意:即使配置了 spring.factories, 也不会实例化)
 * 2.有配置该属性,但属性值不等于 havingValue;
 *
 * 注意:
 * 这里只是启动实例化,启动过程spring并不会检查是否实例化。只有当使用到该bean的时候才知道。
 *
 */
@Configuration
@ConfigurationProperties(prefix = "match-if-missing-false")
@ConditionalOnProperty(prefix = "match-if-missing-false", value = "open", havingValue = "true", matchIfMissing = false)
public class MatchIfMissingFalse {
    private String zhang;
    private boolean open;
}
