
package com.multidb.config.shardingsphere.masterslave;

import org.apache.shardingsphere.core.yaml.config.masterslave.YamlMasterSlaveRuleConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 参考
 * {@link org.apache.shardingsphere.shardingjdbc.spring.boot.masterslave.SpringBootMasterSlaveRuleConfigurationProperties}
 */
@ConfigurationProperties(prefix = "my.shardingsphere.masterslave")
public class MyMasterSlaveRuleConfigurationProps extends YamlMasterSlaveRuleConfiguration {
}