package com.example.use4_nested_properties.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;


@Data
@ConfigurationProperties(prefix = "my4.datasource")
public class AllInfo extends AbstractDatabaseInfo{
    /**
     * 是否开启配置
     */
    private String open = "false";

    /**
     * 默认驱动 mysql
     */
    private String driverClassName = "com.mysql.cj.jdbc.Driver";

    /**
     * 用户信息
     * {@link NestedConfigurationProperty}: 嵌套配置
     */
    @NestedConfigurationProperty
    private UserInfo userInfo;
}
