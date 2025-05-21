package com.config.use;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(
        scanBasePackages = "com.config.use"
)
public class AutoConfigUseApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoConfigUseApplication.class, args);
    }
}