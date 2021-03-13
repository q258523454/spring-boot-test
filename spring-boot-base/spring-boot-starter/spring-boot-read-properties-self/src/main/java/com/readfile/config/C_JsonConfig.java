package com.readfile.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * json配置加载器
 * 方法一: 直接实现 PropertySourceLoader
 *
 * {@link org.springframework.boot.env.PropertySourceLoader]} 接口让springboot自动读取 application.json
 * 定义后去/META-INF/spring.factories配置启动类加载
 * 缺点：命名必须为 application.json
 */
@Data
@Configuration
@ConfigurationProperties(value = "my3")
public class C_JsonConfig {

    private String name;

    @Override
    public String toString() {
        return "C_JsonConfig{" +
                "name='" + name + '\'' +
                '}';
    }
}
