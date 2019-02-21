package com.test.intercept;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created By
 *
 * @author :   zhangjian
 * @date :   2018-08-31
 */
@Configuration
public class MyInterceptConfigurer implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加拦截器
        // **表示当前目录以及所有子目录（递归），/*表示当前目录，不包括子目录
        registry.addInterceptor(new MyIntercept1()).addPathPatterns("/login/**"); // 拦截intercept及其所有子路由
        registry.addInterceptor(new MyIntercept2()).addPathPatterns("/login/**"); // 拦截intercept及其所有子路由

    }
}
