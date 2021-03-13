package com.encryption.util;


import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @date 2020-05-15 9:54
 * @modify
 */
@Slf4j
public enum RSAUtil {
    ;

    /**
     * 算法/模式/补码方式
     */
    public static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";

    /**
     * 密钥位数, 越大越难破解
     */
    private static final int KEY_BIT_NUM = 1024;

    /**
     * 签名算法 可选: MD5withRSA, SHA1withRSA, SHA256withRSA 等等
     */
    public static final String SIGN_ALGORITHM = "SHA1withRSA";
    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "publicKey";

    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "privateKey";


    /**
     * 生成公私钥
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static Map<String, Object> genKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 密钥位数
        keyPairGen.initialize(KEY_BIT_NUM);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * 读取 publicKey
     * @param keyMap
     * @return
     */
    public static String getPublicKey(Map<String, Object> keyMap) {
        Key publicKey = (Key) keyMap.get(PUBLIC_KEY);
        return new BASE64Encoder().encodeBuffer(publicKey.getEncoded());
    }

    /**
     * 读取 privateKey
     * @param keyMap
     */
    public static String getPrivateKey(Map<String, Object> keyMap) {
        Key privateKey = (Key) keyMap.get(PRIVATE_KEY);
        return new BASE64Encoder().encodeBuffer(privateKey.getEncoded());
    }


    /**
     * 私钥加签
     * @param data 内容
     * @param privateKey 私钥
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        // 解码
        byte[] keyBytes = new BASE64Decoder().decodeBuffer(privateKey);
        // 构造 PKCS8EncodedKeySpec (针对 pkcs8 私钥)
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey wPrivateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Signature signature = Signature.getInstance(SIGN_ALGORITHM);
        signature.initSign(wPrivateKey);
        signature.update(data);
        return new BASE64Encoder().encode(signature.sign());
    }

    /**
     * 公钥验签
     * @param data 内容
     * @param publicKey 公钥
     * @param sign 签名
     */
    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
        // 解码
        byte[] keyBytes = new BASE64Decoder().decodeBuffer(publicKey);
        // 构造 X509EncodedKeySpec (针对 pkcs8 私钥)
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey key = keyFactory.generatePublic(x509EncodedKeySpec);
        Signature signature = Signature.getInstance(SIGN_ALGORITHM);
        signature.initVerify(key);
        signature.update(data);
        return signature.verify(new BASE64Decoder().decodeBuffer(sign));
    }

    /**
     * 私钥加密
     * @param data
     * @param privateKey
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
        // 解码
        byte[] keyBytes = new BASE64Decoder().decodeBuffer(privateKey);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key key = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    /**
     * 公钥加密
     * @param data
     * @param publicKey
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
        byte[] keyBytes = new BASE64Decoder().decodeBuffer(publicKey);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key key = keyFactory.generatePublic(x509EncodedKeySpec);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }


    /**
     * 私钥解密
     * @param data
     * @param privateKey
     */
    public static byte[] decryptByPrivateKey(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = new BASE64Decoder().decodeBuffer(privateKey);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key key = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    /**
     * 公钥解密
     * @param data
     * @param publicKey
     */
    public static byte[] decryptByPublicKey(byte[] data, String publicKey) throws Exception {
        byte[] keyBytes = new BASE64Decoder().decodeBuffer(publicKey);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key key = keyFactory.generatePublic(x509EncodedKeySpec);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(data);
    }
}
