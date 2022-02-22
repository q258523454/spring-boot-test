package com.encrypt.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.encrypt.entity.BodyObject;
import com.zhang.encryptbody.annotation.decrypt.AESDecryptBody;
import com.zhang.encryptbody.annotation.encrypt.AESEncryptBody;
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
//@DecryptBody(value = DecryptEnum.AES)
public class AESController {
    private static final Logger logger = LoggerFactory.getLogger(AESController.class);

    /**
     * 密钥的使用优先级:
     * 1.优先使用注解配置
     *   1.1.优先选择方法上的注解@EncryptBody上的密钥
     *   1.2.其次选择类上的注解@EncryptBody上的密钥
     * 2.当没有注解或注解的配置为空的时候, 使用yml配置文件的密钥
     * @param s 待加密字符
     * @return 加密串
     */
    @PostMapping(value = "/aes/encrypt")
    @ResponseBody
    @AESEncryptBody(key = "zIdivNmX09RKO0aiutQ8nQ==")
    public String encrypt(HttpServletRequest request, @RequestBody String s) {
        return s;
    }

    // 解密串必须是 @RequestBody
    @GetMapping(value = "/aes/decrypt")
    @AESDecryptBody(key = "zIdivNmX09RKO0aiutQ8nQ==")
    public String decrypt(HttpServletRequest request, @RequestBody String s) {
        logger.info("body:" + s);
        BodyObject bodyObject = JSONObject.parseObject(s, BodyObject.class);
        logger.info("student is:" + JSON.toJSONString(bodyObject));
        return s;
    }

    @PostMapping(value = "/aes/encrypt2")
    @ResponseBody
    @AESEncryptBody
    public String encrypt2(HttpServletRequest request, @RequestBody String s) {
        return s;
    }

    @GetMapping(value = "/aes/decrypt2")
    @AESDecryptBody
    public String decrypt2(HttpServletRequest request, @RequestBody String s) {
        logger.info("body:" + s);
        BodyObject bodyObject = JSONObject.parseObject(s, BodyObject.class);
        logger.info("student is:" + JSON.toJSONString(bodyObject));
        return s;
    }
}
