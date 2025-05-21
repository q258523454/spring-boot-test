package shiro2.login.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfigurer implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加拦截器
        // **表示当前目录以及所有子目录（递归）, /*表示当前目录，不包括子目录
        // 拦截intercept及其所有子路由
        registry.addInterceptor(new LoginIntercept()).addPathPatterns("/**");
    }
}
