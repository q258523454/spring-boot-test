package com.encrypt.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.encrypt.entity.BodyObject;
import com.zhang.encryptbody.annotation.decrypt.RSADecryptBody;
import com.zhang.encryptbody.annotation.encrypt.RSAEncryptBody;
import com.zhang.encryptbody.util.RSAUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * @author zj
 * @date 2020-05-08 17:23
 */
@RestController
public class RSAController {
    private static final Logger logger = LoggerFactory.getLogger(RSAController.class);

    /**
     * {@link RSAUtil} 先生成非对称密钥
     */
    @PostMapping(value = "/rsa/encrypt")
    @ResponseBody
    @RSAEncryptBody
    public String encrypt(HttpServletRequest request, @RequestBody String s) {
        return s;
    }

    @GetMapping(value = "/rsa/decrypt")
    @RSADecryptBody
    public String decrypt(HttpServletRequest request, @RequestBody String s) {
        logger.info("body:" + s);
        BodyObject bodyObject = JSONObject.parseObject(s, BodyObject.class);
        logger.info("student is:" + JSON.toJSONString(bodyObject));
        return s;
    }
}
