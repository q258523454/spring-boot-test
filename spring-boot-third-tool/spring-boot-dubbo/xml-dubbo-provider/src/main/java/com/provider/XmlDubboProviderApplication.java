package com.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"classpath:dubbo-provider.xml"})
@EnableDubbo
public class XmlDubboProviderApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(XmlDubboProviderApplication.class, args);
    }

}
