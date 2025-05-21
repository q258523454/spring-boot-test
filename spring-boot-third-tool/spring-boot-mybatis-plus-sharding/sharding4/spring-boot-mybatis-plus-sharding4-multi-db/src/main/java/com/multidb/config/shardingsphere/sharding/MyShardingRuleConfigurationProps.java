package com.multidb.config.shardingsphere.sharding;

import org.apache.shardingsphere.core.yaml.config.sharding.YamlShardingRuleConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 参考
 * {@link org.apache.shardingsphere.shardingjdbc.spring.boot.sharding.SpringBootShardingRuleConfigurationProperties}
 */
@ConfigurationProperties(prefix = "my.shardingsphere.sharding")
public class MyShardingRuleConfigurationProps extends YamlShardingRuleConfiguration {

}
