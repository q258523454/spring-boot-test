package com.multidb.config.shardingsphere;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

/**
 * 参考
 * {@link org.apache.shardingsphere.shardingjdbc.spring.boot.common.SpringBootPropertiesConfigurationProperties}
 */
@Data
@ConfigurationProperties(prefix = "my.shardingsphere")
public class MyShardingSphereProps {
    /**
     * my.shardingsphere.props 下的配置, 例如是否开启sql日志 sql.show:true
     */
    private Properties props = new Properties();
}
