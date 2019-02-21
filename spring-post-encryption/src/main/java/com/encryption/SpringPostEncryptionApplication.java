package com.encryption;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@MapperScan("com.dao") // mybatis-dao层
public class SpringPostEncryptionApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringPostEncryptionApplication.class, args);
	}

}

