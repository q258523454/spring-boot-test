package com.encrypt.controller;

import com.zhang.encryptbody.annotation.encrypt.MD5EncryptBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author zj
 * @date 2020-05-08 17:23
 */
@RestController
public class MD5Controller {
    private static final Logger logger = LoggerFactory.getLogger(MD5Controller.class);

    @GetMapping(value = "/md5/encrypt")
    @MD5EncryptBody
    public String md5() {
        return "API市场";
    }
}
