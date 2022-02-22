package com.test.config;

import com.test.intercept.AllIntercept;
import com.test.intercept.LoginIntercept;
import com.test.intercept.resolver.MyMethodArgumentResolverResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfigurer implements WebMvcConfigurer {


    @Autowired
    private AllIntercept allIntercept;

    @Autowired
    private MyMethodArgumentResolverResolver myMethodArgumentResolverResolver;

    @Bean
    public AllIntercept getAllIntercept() {
        return new AllIntercept();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加拦截器
        // **表示当前目录以及所有子目录（递归）, /*表示当前目录，不包括子目录
        registry.addInterceptor(new LoginIntercept()).addPathPatterns("/login/**"); // 拦截intercept及其所有子路由
        registry.addInterceptor(allIntercept).addPathPatterns("/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(myMethodArgumentResolverResolver);
    }
}
