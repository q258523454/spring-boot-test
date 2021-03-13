package com.encryption.util;


import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.util.Map;
import java.util.Properties;

/**
 * @Description
 * @date 2020-05-15 16:43
 * @modify
 */

@Slf4j
public class RSATestMain {
    public static void main(String[] args) throws Exception {

        // -------------------- 生成RSA-公私钥 --------------------
        Map<String, Object> keyPair = RSAUtil.genKeyPair();
        log.info("public key 生成结果:" + RSAUtil.getPublicKey(keyPair));
        log.info("private key生成结果:" + RSAUtil.getPrivateKey(keyPair));

        Properties prop = new Properties();
        prop.load(RSATestMain.class.getClassLoader().getResourceAsStream("RSA_KEY.properties"));
        String publicKey = prop.getProperty("PUBLIC_KEY");
        String privateKey = prop.getProperty("PRIVATE_KEY");

        // -------------------- 场景1: 公钥加密,私钥解密(可能截取公钥,伪造数据) ----------------
        String data = "zhang";
        log.info("data:" + data);
        // 公钥加密
        byte[] encrypt = RSAUtil.encryptByPublicKey(data.getBytes(), publicKey);
        log.info("公钥加密:" + new BASE64Encoder().encodeBuffer(encrypt));
        byte[] decrypt = RSAUtil.decryptByPrivateKey(encrypt, privateKey);
        log.info("私钥解密:" + new String(decrypt));
        // ----------------------------------------------------------------

        // -------------------- 场景2: 私钥签名, 公钥验签(可能截取公钥,获取数据) ----------------
        String data1 = "小A数据";
        String sign1 = RSAUtil.sign(data1.getBytes(), privateKey);
        String encodeEncrypt = new BASE64Encoder().encodeBuffer(RSAUtil.encryptByPrivateKey(data1.getBytes(), privateKey));

        // 因没有私钥无法生成签名
        String data2 = "小B数据";

        boolean verify1 = RSAUtil.verify(data1.getBytes(), publicKey, sign1);
        boolean verify2 = RSAUtil.verify(data2.getBytes(), publicKey, sign1);
        log.info(data1 + "验签结果:" + verify1);
        log.info(data2 + "验签结果:" + verify2);
        if (verify1) {
            log.info("小A数据验签成功,开始解密:");
            byte[] bytes = new BASE64Decoder().decodeBuffer(encodeEncrypt);
            byte[] decryptByPublicKey = RSAUtil.decryptByPublicKey(bytes, publicKey);
            log.info("公钥解密:" + new String(decryptByPublicKey));
        }

        // ----------------- 避免场景1的数据伪造和场景2的数据泄露 -------------------------
        // A,B各有一套RSA-公私钥
        // A持有:A私钥+B公钥
        // B持有:B私钥+A公钥
        // A向B传数据: A用B公钥加密数据,用A私钥对数据签名； B收到数据后用B私钥解密数据,用A公钥对数据验签
        // TODO
    }

}
