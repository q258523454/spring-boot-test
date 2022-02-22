//package com.encrypt.controller;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.encrypt.entity.BodyObject;
//import com.lj08.encryptbody.annotation.decrypt.AESDecryptBody;
//import com.lj08.encryptbody.annotation.decrypt.SM4DecryptBody;
//import com.lj08.encryptbody.annotation.encrypt.SM4EncryptBody;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//
//
///**
// * @author zj
// * @date 2020-05-08 17:23
// */
//@RestController
//public class SM4Controller {
//    private static final Logger logger = LoggerFactory.getLogger(SM4Controller.class);
//
//
//    @PostMapping(value = "/sm4/encrypt")
//    @ResponseBody
//    @SM4EncryptBody(key = "1023456789123456")
//    public String encrypt(HttpServletRequest request, @RequestBody String s) {
//        return s;
//    }
//
//    // 解密串必须是 @RequestBody
//    @GetMapping(value = "/sm4/decrypt")
//    @SM4DecryptBody(key = "1023456789123456")
//    public String decrypt(HttpServletRequest request, @RequestBody String s) {
//        logger.info("body:" + s);
//        BodyObject bodyObject = JSONObject.parseObject(s, BodyObject.class);
//        logger.info("student is:" + JSON.toJSONString(bodyObject));
//        return s;
//    }
//
//    @PostMapping(value = "/sm4/encrypt2")
//    @ResponseBody
//    @SM4EncryptBody
//    public String encrypt2(HttpServletRequest request, @RequestBody String s) {
//        return s;
//    }
//
//    @GetMapping(value = "/sm4/decrypt2")
//    @SM4DecryptBody
//    public String decrypt2(HttpServletRequest request, @RequestBody String s) {
//        logger.info("body:" + s);
//        BodyObject bodyObject = JSONObject.parseObject(s, BodyObject.class);
//        logger.info("student is:" + JSON.toJSONString(bodyObject));
//        return s;
//    }
//}
