package com.sharding5multidb.config.shardingsphere;

import lombok.Getter;
import lombok.Setter;

import org.apache.shardingsphere.infra.yaml.config.pojo.mode.YamlModeConfiguration;
import org.apache.shardingsphere.spring.boot.prop.SpringBootPropertiesConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "my.shardingsphere")
public class MySpringBootPropertiesConfiguration {
    /**
     * 全局配置,参考
     * {@link SpringBootPropertiesConfiguration}
     */
    private Properties props = new Properties();

    private YamlModeConfiguration mode;
}
