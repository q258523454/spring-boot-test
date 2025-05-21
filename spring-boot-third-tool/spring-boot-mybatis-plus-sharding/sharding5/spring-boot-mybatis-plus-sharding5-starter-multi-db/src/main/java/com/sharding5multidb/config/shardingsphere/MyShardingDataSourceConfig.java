
package com.sharding5multidb.config.shardingsphere;


import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.mode.ModeConfiguration;
import org.apache.shardingsphere.infra.config.rule.RuleConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import javax.sql.DataSource;

@Configuration
public class MyShardingDataSourceConfig {

    @Autowired
    private MyShardingDataSourceProps dataSourceProps;

    @Autowired
    private MySpringBootPropertiesConfiguration props;

    @Autowired
    private MybatisPlusInterceptor mybatisPlusInterceptor;


    @Bean(name = "myShardingSphereDataSource")
    public DataSource myShardingSphereDataSource(final ObjectProvider<List<RuleConfiguration>> rules, final ObjectProvider<ModeConfiguration> modeConfig) throws SQLException {
        Collection<RuleConfiguration> ruleConfigs = Optional.ofNullable(rules.getIfAvailable()).orElseGet(Collections::emptyList);
        return ShardingSphereDataSourceFactory.createDataSource(dataSourceProps.getDatabaseName(), modeConfig.getIfAvailable(), dataSourceProps.getDataSourceMap(), ruleConfigs, props.getProps());
    }

    /**
     * Sharding 事务管理器
     */
    @Bean(name = "myShardingTransactionManager")
    public PlatformTransactionManager myTransactionManager(@Qualifier("myShardingSphereDataSource") DataSource dynamicDataSource) {
        return new DataSourceTransactionManager(dynamicDataSource);
    }

    /**
     * SqlSessionFactory
     * com.baomidou.mybatisplus.core.metadata.TableInfoHelper#initTableInfo(org.apache.ibatis.builder.MapperBuilderAssistant, java.lang.Class)
     */
    @Bean(name = "myShardingSqlSessionFactory")
    public SqlSessionFactory getMybatisSqlSessionFactory(@Qualifier("myShardingSphereDataSource") DataSource myDataSource) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(myDataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        // 定义多个 sqlSessionFactory 的时候注意 mapper 要指定子目录，否则会 MybatisPlus 会出现 sqlSessionFactory 不正确。
        // 原因详见: TableInfoHelper.initTableInfo()
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath*:mapper/sharding/**/*Mapper.xml"));
        sqlSessionFactoryBean.setConfigLocation(new DefaultResourceLoader().getResource("classpath:mybatis-config.xml"));
        List<Interceptor> interceptors = new ArrayList<>();
        interceptors.add(mybatisPlusInterceptor);
        sqlSessionFactoryBean.setPlugins(interceptors.toArray(new Interceptor[0]));
        Properties properties = new Properties();
        properties.put("dialect", "mysql");
        sqlSessionFactoryBean.setConfigurationProperties(properties);
        return sqlSessionFactoryBean.getObject();
    }

}
