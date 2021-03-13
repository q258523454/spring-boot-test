package com.encrypt;

import com.zhang.encryptbody.annotation.EnableEncryptBody;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptBody
public class SpringEncryptUseApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringEncryptUseApplication.class, args);
    }
}

