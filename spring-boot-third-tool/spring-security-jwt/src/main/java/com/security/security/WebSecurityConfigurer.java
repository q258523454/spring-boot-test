package com.security.security;

import com.security.filter.BasicAuthFilter;
import com.security.filter.JWTLoginFilter;
import com.security.filter.LoginAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created By
 *
 * @date :   2018-11-05
 */


@Configuration
@EnableWebSecurity
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    // API-白名单, 注意:最后的","逗号不要遗漏
    private final String[] AUTH_WHITELIST = {
            "/test",
            "/insertUser",
    };

    @Autowired
    @Qualifier("userDetailsServiceImpl")
    private UserDetailsService userDetailsService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // 定义安全策略
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // http.cors().and().csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)?
        http.cors().and()
                .csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()    // 先定义白名单,请求不需要验证的URL
                .anyRequest().authenticated()
//                .and()
//                .exceptionHandling()                        // 加了这个之后, 只要是未登录的情况下访问(任何未开放)的接口，都是401
//                .authenticationEntryPoint(new HTTP_401("Test Authority"))
                .and()
                .logout()                                   // 默认注销行为为logout，可以通过下面的方式来修改
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")                 // 设置注销成功后跳转页面，默认是跳转到登录页面;
                .permitAll()
                .and()
                .formLogin()
                .and()
                .addFilter(new JWTLoginFilter(authenticationManager()))
                .addFilter(new BasicAuthFilter(authenticationManager()));
    }


    // 指定登录过滤器
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new LoginAuthenticationProvider(userDetailsService, bCryptPasswordEncoder));
    }
}

