package com.quartz.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据源配置
 */
@Configuration
public class QuartzDataSourceConfig {

    private Logger log = LoggerFactory.getLogger(this.getClass());


    // 默认数据库操作为主数据库
    @Primary
    @Bean(name = "dbMaster")
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource dataSource() {
        log.info("master 数据源");
        return new DruidDataSource();
    }

    @Bean(name = "dbQuartz")
    @ConfigurationProperties(prefix = "spring.datasource.quartz")
    public DataSource quartzDataSource() {
        log.info(" quartz 数据源");
        return new DruidDataSource();
    }


    // 注入 quartz 数据源
    @Bean(name = "quartzTransactionManager")
    public PlatformTransactionManager quartzTransactionManager(@Qualifier("dbQuartz") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
