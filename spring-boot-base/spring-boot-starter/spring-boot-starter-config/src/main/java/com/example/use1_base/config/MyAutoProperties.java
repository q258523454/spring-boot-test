package com.example.use1_base.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "my1")
public class MyAutoProperties {

    private String msg = "zhang";

    private boolean open = false;

}
