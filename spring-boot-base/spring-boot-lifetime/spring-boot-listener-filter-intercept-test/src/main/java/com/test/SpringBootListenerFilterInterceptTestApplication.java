package com.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan // filter、servlet都需要加上这个注解
public class SpringBootListenerFilterInterceptTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootListenerFilterInterceptTestApplication.class, args);
	}

}
