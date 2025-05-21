package com.readfile.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PropertySourceFactory;


/**
 * 读取指定配置文件 my.properties, 使用默认的 PropertySourceFactory (可通过实现这个接口,完成自定义配置读取)
 */
@Data
@Configuration
@PropertySource(value = "classpath:/config/my.properties", factory = PropertySourceFactory.class)
@ConfigurationProperties(value = "my2")
public class B_PropertiesConfig {

    private String name;

    @Override
    public String toString() {
        return "MyPropertiesConfig{" +
                "name='" + name + '\'' +
                '}';
    }
}
