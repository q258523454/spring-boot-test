package com.test.intercept;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created By
 *
 * @date :   2018-08-31
 */
@Configuration
public class WebConfigurer implements WebMvcConfigurer {


    @Autowired
    private IpAddressIntercept ipAddressIntercept;

    @Bean
    public IpAddressIntercept getIpAddressIntercept() {
        return new IpAddressIntercept();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加拦截器
        // **表示当前目录以及所有子目录（递归）, /*表示当前目录，不包括子目录
        registry.addInterceptor(new MyIntercept()).addPathPatterns("/login/**"); // 拦截intercept及其所有子路由
        registry.addInterceptor(ipAddressIntercept).addPathPatterns("/**");

    }
}
