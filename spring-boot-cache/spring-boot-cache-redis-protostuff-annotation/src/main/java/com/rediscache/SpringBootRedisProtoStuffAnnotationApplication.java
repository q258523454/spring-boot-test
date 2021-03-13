package com.rediscache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.rediscache.com.multi.dao")
public class SpringBootRedisProtoStuffAnnotationApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootRedisProtoStuffAnnotationApplication.class, args);
	}

}
