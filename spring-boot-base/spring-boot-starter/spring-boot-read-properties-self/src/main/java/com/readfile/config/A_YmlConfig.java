package com.readfile.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: zhangj
 * @Date: 2019-09-11
 * @Version 1.0
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "my")
public class A_YmlConfig {

    private String name;

    @Override
    public String toString() {
        return "ApplicationYmlConfig{" +
                "name='" + name + '\'' +
                '}';
    }
}
