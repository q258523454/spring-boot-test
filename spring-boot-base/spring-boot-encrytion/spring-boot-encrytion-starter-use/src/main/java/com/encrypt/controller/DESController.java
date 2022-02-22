package com.encrypt.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.encrypt.entity.BodyObject;
import com.zhang.encryptbody.annotation.decrypt.DESDecryptBody;
import com.zhang.encryptbody.annotation.encrypt.DESEncryptBody;
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
public class DESController {
    private static final Logger logger = LoggerFactory.getLogger(DESController.class);

    @PostMapping(value = "/des/encrypt")
    @ResponseBody
    @DESEncryptBody
    public String encrypt(HttpServletRequest request, @RequestBody String s) {
        return s;
    }

    @GetMapping(value = "/des/decrypt")
    @DESDecryptBody
    public String decrypt(HttpServletRequest request, @RequestBody String s) {
        logger.info("body:" + s);
        BodyObject bodyObject = JSONObject.parseObject(s, BodyObject.class);
        logger.info("student is:" + JSON.toJSONString(bodyObject));
        return s;
    }
}
