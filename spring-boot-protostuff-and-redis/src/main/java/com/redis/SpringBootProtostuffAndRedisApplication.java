package com.redis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.redis.dao")
public class SpringBootProtostuffAndRedisApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootProtostuffAndRedisApplication.class, args);
	}

}
