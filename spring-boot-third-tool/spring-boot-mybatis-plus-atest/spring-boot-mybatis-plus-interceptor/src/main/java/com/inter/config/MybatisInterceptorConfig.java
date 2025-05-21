
package com.inter.config;

import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import com.inter.config.intercept.MybatisIntercept_DataScope;

import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 分页插件拦截必须优先加载，否则 mybatis 常规分页查询的时候会执行两次拦截
 * 由于 PageHelperAutoConfiguration 在外部 spring.factories 中.
 * 因此需要将该类也放到 spring.factories, 然后指定 @AutoConfigureAfter
 * AutoConfigureAfter-能够改变spring.factories中的bean加载顺序
 */
@Slf4j
@AutoConfigureAfter(PageHelperAutoConfiguration.class)
public class MybatisInterceptorConfig implements InitializingBean {

    public MybatisInterceptorConfig() {
        log.info("init MybatisInterceptorConfig");
    }

    @Autowired
    private List<SqlSessionFactory> sqlSessionFactoryList;

    @Override
    public void afterPropertiesSet() {
        // 创建自定义mybatis拦截器，添加到chain的最后面
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.getConfiguration();
            // 添加自定义拦截器 (SQL拦截器)
            // configuration.addInterceptor(new MybatisIntercept_SqlLog());
            // 添加自定义拦截器 (数据权限处理拦截器)
            configuration.addInterceptor(new MybatisIntercept_DataScope());
        }
    }
}
