//package com.encrypt.controller;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.encrypt.entity.BodyObject;
//import com.lj08.encryptbody.annotation.decrypt.SM2DecryptBody;
//import com.lj08.encryptbody.annotation.encrypt.SM2EncryptBody;
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
//public class SM2Controller {
//    private static final Logger logger = LoggerFactory.getLogger(SM2Controller.class);
//
//    /**
//     * 密钥的使用优先级:
//     * 1.优先使用注解配置
//     *   1.1.优先选择方法上的注解@EncryptBody上的密钥
//     *   1.2.其次选择类上的注解@EncryptBody上的密钥
//     * 2.当没有注解或注解的配置为空的时候, 使用yml配置文件的密钥
//     * @param s 待加密body字符串
//     * @return 加密串
//     */
//    @PostMapping(value = "/sm2/encrypt")
//    @ResponseBody
//    @SM2EncryptBody
//    public String encrypt(HttpServletRequest request, @RequestBody String s) {
//        return s;
//    }
//
//    @GetMapping(value = "/sm2/decrypt")
//    @SM2DecryptBody
//    public String decrypt(HttpServletRequest request, @RequestBody String s) {
//        logger.info("body:" + s);
//        BodyObject bodyObject = JSONObject.parseObject(s, BodyObject.class);
//        logger.info("student is:" + JSON.toJSONString(bodyObject));
//        return s;
//    }
//}
