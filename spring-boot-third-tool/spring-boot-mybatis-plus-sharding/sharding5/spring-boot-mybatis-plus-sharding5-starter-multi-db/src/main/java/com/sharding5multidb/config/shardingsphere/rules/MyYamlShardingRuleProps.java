package com.sharding5multidb.config.shardingsphere.rules;

import lombok.Getter;
import lombok.Setter;

import org.apache.shardingsphere.sharding.spring.boot.rule.YamlShardingRuleSpringBootConfiguration;
import org.apache.shardingsphere.sharding.yaml.config.YamlShardingRuleConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 参考
 * {@link YamlShardingRuleSpringBootConfiguration}
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "my.shardingsphere.rules")
public class MyYamlShardingRuleProps {
    
    private YamlShardingRuleConfiguration sharding;
}