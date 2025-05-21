package com;

import com.multidb.config.shardingsphere.MyShardingSphereProps;
import com.multidb.config.shardingsphere.masterslave.MyMasterSlaveRuleConfigurationProps;
import com.multidb.config.shardingsphere.sharding.MyShardingRuleConfigurationProps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

// 禁用掉默认的数据源获取方式，默认会读取配置文件的据源（spring.datasource.*）
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableConfigurationProperties({MyShardingSphereProps.class, MyShardingRuleConfigurationProps.class,
        MyMasterSlaveRuleConfigurationProps.class})
// 开启 CGLIB 代理
@EnableCaching(proxyTargetClass = true)
public class MybatisPlusSharding4MultiDSApplication {
    public static void main(String[] args) {
        SpringApplication.run(MybatisPlusSharding4MultiDSApplication.class, args);
    }
}