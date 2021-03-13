package com.example.entity;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: zhangj
 * @Date: 2019-09-11
 * @Version 1.0
 */

@Data
@ConfigurationProperties(prefix = "my")
public class AutoConfigEntity {
    private static final Logger logger = LoggerFactory.getLogger(AutoConfigEntity.class);

    private String msg = "zhang";

    private boolean open = false;
}
