package com.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * quartz的初始化配置
 *
 * @Date: 2019-05-29
 * @Version 1.0
 */
@Configuration
public class QuartzConfiguration {

    private DataSource dataSource;
    private PlatformTransactionManager quartzTransactionManager;

    @Autowired
    public QuartzConfiguration(@Qualifier("dbQuartz") DataSource dataSource,@Qualifier("dbMaster") DataSource dbMaster, @Qualifier("quartzTransactionManager") PlatformTransactionManager quartzTransactionManager) {
        this.dataSource = dataSource;
        this.quartzTransactionManager = quartzTransactionManager;
    }

    @Bean
    public SchedulerFactoryBeanCustomizer schedulerFactoryBeanCustomizer() {
        return schedulerFactoryBean -> {
            schedulerFactoryBean.setDataSource(dataSource);
            schedulerFactoryBean.setTransactionManager(quartzTransactionManager);

        };
    }

}