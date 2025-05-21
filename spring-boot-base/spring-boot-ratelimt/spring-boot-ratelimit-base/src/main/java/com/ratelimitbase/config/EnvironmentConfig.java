package com.ratelimitbase.config;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * environment
 *
 * @author zz/z0zz
 * @since 2023-06-14 11:42
 */
@Slf4j
@Configuration
public class EnvironmentConfig {
    /**
     * environment.getRequiredProperty()
     * 不管是 properties 还是apollo配置中心 都可以读取到, 与配置来源无关, 本身就是读取到环境的值
     * 理论上配置中心推送完成后,environment就发生了改变,应该是动态更新的(待测试)
     */
    @Autowired
    private Environment environment;

    public String getPropertyValue(String key) {
        return getPropertyValue(key, true);
    }

    public String getPropertyValue(String key, boolean isLoggerError) {
        String value = null;
        try {
            value = environment.getRequiredProperty(key);
        } catch (IllegalStateException ex) {
            if (isLoggerError) {
                log.error("Can not find properties key:[{}]", key);
            }
        }
        return value;
    }
}
