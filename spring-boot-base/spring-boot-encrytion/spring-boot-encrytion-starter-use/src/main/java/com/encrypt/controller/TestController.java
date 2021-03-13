package com.encrypt.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.encrypt.entity.BodyObject;
import com.zhang.encryptbody.annotation.decrypt.DecryptBody;
import com.zhang.encryptbody.annotation.encrypt.EncryptBody;
import com.zhang.encryptbody.enums.DecryptEnum;
import com.zhang.encryptbody.enums.EncryptEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * @Description
 * @date 2020-05-08 17:23
 * @modify
 */
@RestController
//@DecryptBody(value = DecryptEnum.AES)
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @PostMapping(value = "/encrypt")
    @ResponseBody
    @EncryptBody(value = EncryptEnum.AES, key = "bfcf1ba4d7059352613588effbfe8f0b")
    public String aes(HttpServletRequest request, @RequestBody String s) {
        logger.info("header key:" + request.getHeader("key"));
        logger.info("params:" + JSON.toJSONString(request.getParameterMap()));
        return s;

    }

    @GetMapping(value = "/decrypt")
    // 解密串必须是 @RequestBody
    @DecryptBody(value = DecryptEnum.AES, key = "bfcf1ba4d7059352613588effbfe8f0b")
    public String decrypt(HttpServletRequest request, @RequestBody String s) {
        logger.info("header key:" + request.getHeader("key"));
        logger.info("params:" + JSONObject.toJSONString(request.getParameterMap()));
        logger.info("body:" + s);
        BodyObject bodyObject = JSONObject.parseObject(s, BodyObject.class);
        logger.info("student is:" + JSON.toJSONString(bodyObject));
        return s;
    }

    @GetMapping(value = "/md5")
    @EncryptBody(value = EncryptEnum.MD5)
    public String md5() {
        return "zhang";
    }

}
