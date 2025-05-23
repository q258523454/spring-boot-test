package com.rediscache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan("com.rediscache.dao")
@EnableCaching
public class SpringBootRedisProtoStuffAnnotationApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootRedisProtoStuffAnnotationApplication.class, args);
	}

}
