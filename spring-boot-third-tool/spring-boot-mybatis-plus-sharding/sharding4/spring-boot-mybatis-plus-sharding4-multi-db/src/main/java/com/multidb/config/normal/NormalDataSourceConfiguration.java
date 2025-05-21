package com.multidb.config.normal;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariDataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "com.multidb.dao.normal", sqlSessionFactoryRef = "myNormalSqlSessionFactory")
public class NormalDataSourceConfiguration {

    @Autowired
    private MybatisPlusInterceptor mybatisPlusInterceptor;

    @Autowired
    private MyNormalDataSourceProps myNormalDataSourceProps;

    @Primary
    @Bean(name = "myNormalDataSource")
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .driverClassName(myNormalDataSourceProps.getDriverClassName())
                .username(myNormalDataSourceProps.getUsername())
                .password(myNormalDataSourceProps.getPassword())
                .url(myNormalDataSourceProps.getUrl())
                .build();
    }

    /**
     * SqlSessionFactory
     */
    @Primary
    @Bean(name = "myNormalSqlSessionFactory")
    public SqlSessionFactory getMybatisSqlSessionFactory(@Qualifier("myNormalDataSource") DataSource myDataSource) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(myDataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        // 定义多个 sqlSessionFactory 的时候注意 mapper 要指定子目录，否则会 MybatisPlus 会出现 sqlSessionFactory 不正确。
        // 原因详见: TableInfoHelper.initTableInfo()
        bean.setMapperLocations(resolver.getResources("classpath*:mapper/normal/**/*Mapper.xml"));
        bean.setConfigLocation(new DefaultResourceLoader().getResource("classpath:mybatis-config.xml"));
        List<Interceptor> interceptors = new ArrayList<>();
        interceptors.add(mybatisPlusInterceptor);
        bean.setPlugins(interceptors.toArray(new Interceptor[0]));
        Properties properties = new Properties();
        properties.put("dialect", "mysql");
        bean.setConfigurationProperties(properties);
        return bean.getObject();
    }

    @Primary
    @Bean(name = "myNormalTransactionManager")
    public DataSourceTransactionManager testTransactionManager(@Qualifier("myNormalDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
