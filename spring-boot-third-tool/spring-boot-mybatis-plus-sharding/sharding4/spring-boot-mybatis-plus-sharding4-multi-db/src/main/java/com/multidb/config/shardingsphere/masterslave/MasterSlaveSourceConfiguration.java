package com.multidb.config.shardingsphere.masterslave;


import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.multidb.config.shardingsphere.MyShardingSphereProps;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.shardingsphere.core.yaml.swapper.MasterSlaveRuleConfigurationYamlSwapper;
import org.apache.shardingsphere.shardingjdbc.api.MasterSlaveDataSourceFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "com.multidb.dao.masterslave", sqlSessionFactoryRef = "myMasterSlaveSqlSessionFactory")
public class MasterSlaveSourceConfiguration {

    @Autowired
    private MyShardingSphereProps myProps;

    @Autowired
    private MyMasterSlaveRuleConfigurationProps myMasterSlaveRule;

    @Autowired
    private MyMasterSlaveDataSourceProps myMasterSlaveDataSourceProps;

    @Autowired
    private MybatisPlusInterceptor mybatisPlusInterceptor;

    /**
     * MasterSlave 数据源
     * 参考 {@link org.apache.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration#masterSlaveDataSource}
     */
    @Bean(name = "myMasterSlaveDataSource")
    public DataSource masterSlaveDataSource() throws SQLException {
        return MasterSlaveDataSourceFactory.createDataSource(myMasterSlaveDataSourceProps.getDataSourceMap(),
                new MasterSlaveRuleConfigurationYamlSwapper().swap(myMasterSlaveRule), myProps.getProps());
    }

    /**
     * MasterSlave 事务管理器
     */
    @Bean(name = "myMasterSlaveTransactionManager")
    public PlatformTransactionManager myTransactionManager(@Qualifier("myMasterSlaveDataSource") DataSource dynamicDataSource) {
        return new DataSourceTransactionManager(dynamicDataSource);
    }

    /**
     * SqlSessionFactory
     * com.baomidou.mybatisplus.core.metadata.TableInfoHelper#initTableInfo(org.apache.ibatis.builder.MapperBuilderAssistant, java.lang.Class)
     */
    @Bean(name = "myMasterSlaveSqlSessionFactory")
    public SqlSessionFactory getMybatisSqlSessionFactory(@Qualifier("myMasterSlaveDataSource") DataSource myDataSource) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(myDataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        // 定义多个 sqlSessionFactory 的时候注意 mapper 要指定子目录，否则会 MybatisPlus 会出现 sqlSessionFactory 不正确。
        // 原因详见: TableInfoHelper.initTableInfo()
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath*:mapper/masterslave/**/*Mapper.xml"));
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
