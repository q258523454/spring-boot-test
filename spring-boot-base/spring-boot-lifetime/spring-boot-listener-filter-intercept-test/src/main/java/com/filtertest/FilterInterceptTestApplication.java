package com.filtertest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan // filter、servlet都需要加上这个注解
public class FilterInterceptTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(FilterInterceptTestApplication.class, args);
	}

}
