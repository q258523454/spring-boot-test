package com.mongobase;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan({
        "com.mongobase.**"
})
@Slf4j
public class MongoBaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(MongoBaseApplication.class, args);
    }

}