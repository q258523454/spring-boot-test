package com.zhang.encryptbody.util;


import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Properties;

/**
 * @Description
 * @date 2020-05-15 16:43
 * @modify
 */

@Slf4j
public class Test_RSA {
    /**
     * RSA_KEY.properties文件内容如下：
     *
     * PUBLIC_KEY:MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDFqfLePf2ccQwIpu2r328DUCjBHkOfHPhyLZxf\
     * o50QCEliBVVyLA2Cih/3vP5kIXecFlIraOSOXFKzS3nzTuvkdoTOwtuyMLz/WNu9H2PJFYUZ1Hps\
     * LGihdLsoNhYlckCpYivuXtUWcc8HETRmawpEFHgoanPBtUpO7x1jgKILhwIDAQAB
     *
     * PRIVATE_KEY:MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBAMWp8t49/ZxxDAim7avfbwNQKMEe\
     * Q58c+HItnF+jnRAISWIFVXIsDYKKH/e8/mQhd5wWUito5I5cUrNLefNO6+R2hM7C27IwvP9Y270f\
     * Y8kVhRnUemwsaKF0uyg2FiVyQKliK+5e1RZxzwcRNGZrCkQUeChqc8G1Sk7vHWOAoguHAgMBAAEC\
     * gYEAmDoSOOY08s59Hgyw7bTdogeR22ibW2bMT+Kgk2dGBprbKQpCi/MxVF9bz0dWA3dizmrCTXwy\
     * yRCBAGQYPzGAqM+eQ5dlBDGD1/zMvhuC4qR5kan93lVc6lyINEshi+iT5MRoHwnklxN7CIPUlg5U\
     * or4hu+2otgLWx+Fne3IDaQECQQDnaBqZwqzo1Cb2dhtzPHbjOYUx1YZUjlC0JpQmKxgcurrlAopi\
     * SHKFaUjtO73eEDqA3iKh6XnkD9ZpUi4xP8f3AkEA2qvNaUmSsvOMyUayeFTwXmGKzDKCfpEoFjmu\
     * isFszQX3IVoQwMLYoe2S+O8SmqMygTKU3FvXhdpKJGmONOSU8QJBAOALbxXH4llabo7ZMyKTPBgr\
     * rMXfEjvzvjdt0qpRtvq54BGyMAGQWzDICw+FdAL4e2whpi5VOuqQWuuF4atVMWkCQQChfQU6v0He\
     * dGzBbDt7EEdffCuzt42ViP+QwlJ7WHNQShqC/++lW3/+veOvgWc82k7/COrUfeMBj/73OxjxSOoR\
     * AkEAyfi8P+rU5K1AT4RTO8nDL76qHy2ASEu0K6nLlGsa2AH09fbKCb050FXiOQlimEfKNzxkRwSn\
     * gBqL7If8RJgEEw==
     */
    public static void main(String[] args) throws Exception {

        // -------------------- 生成RSA-公私钥 --------------------
        Map<String, Object> keyPair = RSAUtil.genKeyPair(1024);
        log.info("private key 生成结果:{}", RSAUtil.getPrivateKey(keyPair));

        // -------------------- 读取配置公私钥 --------------------
        Properties prop = new Properties();
        prop.load(Test_RSA.class.getClassLoader().getResourceAsStream("RSA_KEY_A.properties"));
        String publicKey = prop.getProperty("PUBLIC_KEY");
        String privateKey = prop.getProperty("PRIVATE_KEY");
        log.info("公钥:{}", publicKey);
        log.info("私钥:{}", privateKey);


        // -------------------- 场景1: 公钥加密,私钥解密(可能截取公钥,伪造数据) ----------------
        String data = "API原始数据";
        log.info("数据:" + data);
        // 公钥加密
        String encrypt = RSAUtil.encrypt(data.getBytes(), publicKey);
        log.info("公钥加密:{}", encrypt);
        // 私钥解密
        String decrypt = RSAUtil.decrypt(encrypt, privateKey);
        log.info("私钥解密:{}", decrypt);

        // ----------------------------------------------------------------

        // -------------------- 场景2: 私钥签名, 公钥验签(可能截取公钥,获取数据) ----------------
        String encryptByPrivateKey = RSAUtil.encryptByPrivateKey(data.getBytes(), privateKey);
        log.info("私钥加密:{}", encryptByPrivateKey);

        String sign = RSAUtil.sign(encryptByPrivateKey.getBytes(), privateKey, "SHA1withRSA");
        log.info("私钥签名:{}", sign);

        boolean verify = RSAUtil.verify(encryptByPrivateKey.getBytes(), publicKey, sign, "SHA1withRSA");
        log.info(data + "验签结果:" + verify);
        if (verify) {
            // 公钥解密
            String decryptData = RSAUtil.decryptByPublicKey(encryptByPrivateKey, publicKey);
            log.info("公钥解密:{}", decryptData);
        }

        // ----------------- 场景3(时间较长)：可避免数据伪造(场景1)和数据泄露(场景2)-------------------------
        // A,B各有一套RSA-公私钥
        // A持有:A私钥+B公钥
        // B持有:B私钥+A公钥
        // A向B传数据: A用B公钥加密数据,用A私钥对数据签名； B收到数据后用B私钥解密数据,用A公钥对数据验签
        // TODO

    }

}
