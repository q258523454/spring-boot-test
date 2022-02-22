package com.zhang.encryptbody.util;


import com.zhang.encryptbody.enums.ModeEnum;
import com.zhang.encryptbody.enums.PaddingEnum;
import com.zhang.encryptbody.enums.RSASignTypeEnum;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zj
 * @date 2020-05-15 9:54
 */
@Slf4j
public enum RSAUtil {
    ;

    private static final String RSA = "RSA";

    /**
     * 生成公私钥的位数, 越大越难破解
     */
    private static final int KEY_BIT_NUM = 1024;

    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "publicKey";

    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "privateKey";


    /**
     * 生成公私钥, 默认密钥长度: 1024
     * @return map密钥对
     */
    public static Map<String, Object> genKeyPair() throws NoSuchAlgorithmException {
        return genKeyPair(KEY_BIT_NUM);
    }

    /**
     * 生成公私钥
     * @param keyBitLength 密钥位数, 越大越难破解
     * @return map密钥对
     */
    public static Map<String, Object> genKeyPair(int keyBitLength) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(RSA);
        // 密钥位数
        keyPairGen.initialize(keyBitLength);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * 获取 publicKey
     * @param keyMap 密钥对
     * @return 十六进制字符
     */
    public static String getPublicKey(Map<String, Object> keyMap) {
        Key publicKey = (Key) keyMap.get(PUBLIC_KEY);
        return BASE64Util.encode(publicKey.getEncoded());
    }

    /**
     * 获取 privateKey
     * @param keyMap 密钥对
     * @return 十六进制字符
     */
    public static String getPrivateKey(Map<String, Object> keyMap) {
        Key privateKey = (Key) keyMap.get(PRIVATE_KEY);
        return BASE64Util.encode(privateKey.getEncoded());
    }

    /**
     * 公钥加密
     * 默认模式:ECB/PKCS1Padding
     * @param data 原数据
     * @param publicKey 公钥
     */
    public static String encrypt(byte[] data, String publicKey) {
        return encrypt(data, publicKey, ModeEnum.ECB.getCode(), PaddingEnum.PKCS1Padding.getCode());
    }

    /**
     * 公钥加密.
     * @param data the 原数据
     * @param publicKey 公钥
     * @param mode 算法模式
     * @param padding 补码方式
     */
    public static String encrypt(byte[] data, String publicKey, String mode, String padding) {
        try {
            Cipher cipher = Cipher.getInstance(RSA + "/" + mode + "/" + padding);
            Key key = generateKeyByPublicKey(publicKey);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] bytes = cipher.doFinal(data);
            return BASE64Util.encode(bytes);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 私钥加密.
     * 默认模式:ECB/PKCS1Padding
     * @param data the 原数据
     * @param privateKey 私钥
     */
    public static String encryptByPrivateKey(byte[] data, String privateKey) {
        return encryptByPrivateKey(data, privateKey, ModeEnum.ECB.getCode(), PaddingEnum.PKCS1Padding.getCode());
    }

    /**
     * 私钥加密.
     * @param data the 原数据
     * @param privateKey 私钥
     * @param mode 算法模式
     * @param padding 补码方式
     */
    public static String encryptByPrivateKey(byte[] data, String privateKey, String mode, String padding) {
        try {
            Cipher cipher = Cipher.getInstance(RSA + "/" + mode + "/" + padding);
            Key key = generateKeyByPrivateKey(privateKey);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] bytes = cipher.doFinal(data);
            return BASE64Util.encode(bytes);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    /**
     * 私钥解密
     * 默认模式:ECB/PKCS1Padding
     * @param data the 待处理BASE64字符串
     * @param privateKey 私钥
     */
    public static String decrypt(String data, String privateKey) {
        return decrypt(data, privateKey, ModeEnum.ECB.getCode(), PaddingEnum.PKCS1Padding.getCode());
    }

    /**
     * 私钥解密
     * @param data the 待处理BASE64字符串
     * @param privateKey 私钥
     * @param mode 算法模式
     * @param padding 补码方式
     */
    public static String decrypt(String data, String privateKey, String mode, String padding) {
        try {
            Cipher cipher = Cipher.getInstance(RSA + "/" + mode + "/" + padding);
            Key key = generateKeyByPrivateKey(privateKey);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] bytes = cipher.doFinal(BASE64Util.decode(data));
            return new String(bytes);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 公钥解密
     * 默认模式:ECB/PKCS1Padding
     * @param data the 待处理BASE64字符串
     * @param publicKey 公钥
     */
    public static String decryptByPublicKey(String data, String publicKey) {
        return decryptByPublicKey(data, publicKey, ModeEnum.ECB.getCode(), PaddingEnum.PKCS1Padding.getCode());
    }

    /**
     * 公钥解密
     * @param data the 原数据
     * @param publicKey 公钥
     * @param mode 算法模式
     * @param padding 补码方式
     */
    public static String decryptByPublicKey(String data, String publicKey, String mode, String padding) {
        try {
            Cipher cipher = Cipher.getInstance(RSA + "/" + mode + "/" + padding);
            Key key = generateKeyByPublicKey(publicKey);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] bytes = cipher.doFinal(BASE64Util.decode(data));
            return new String(bytes);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 私钥加签, 即对原文的摘要进行加密
     * 默认签名算法: SHA1withRSA
     * @param data 内容/原文
     * @param privateKey 私钥
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        return sign(data, privateKey, RSASignTypeEnum.SHA1withRSA.getCode());
    }

    /**
     * 私钥加签, 即对原文的摘要进行加密
     * @param data 内容/原文
     * @param privateKey 私钥
     * @param signAlgorithm 签名算法,默认:SHA1withRSA,可选: MD5withRSA, SHA1withRSA, SHA256withRSA 等等
     */
    public static String sign(byte[] data, String privateKey, String signAlgorithm) throws Exception {
        Key key = generateKeyByPrivateKey(privateKey);
        Signature signature = Signature.getInstance(signAlgorithm);
        signature.initSign((PrivateKey) key);
        signature.update(data);
        return BASE64Util.encode(signature.sign());
    }

    /**
     * 公钥验签, 即对签名解密后对比原文摘要,是否相等
     * 默认签名算法: SHA1withRSA
     * @param data 内容/原文
     * @param publicKey 公钥
     * @param sign 签名
     */
    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
        // 可选:MD2withRSA,SHA1withRSA,SHA256withRSA,SHA384withRSA,SHA512withRSA 等等
        return verify(data, publicKey, sign, RSASignTypeEnum.SHA1withRSA.getCode());
    }

    /**
     *
     * 公钥验签, 即对签名解密后对比原文摘要,是否相等
     * @param data 内容/原文
     * @param publicKey 公钥
     * @param sign 签名
     * @param signAlgorithm 签名算法,默认:SHA1withRSA,可选: MD5withRSA, SHA1withRSA, SHA256withRSA 等等
     */
    public static boolean verify(byte[] data, String publicKey, String sign, String signAlgorithm) throws Exception {
        Key key = generateKeyByPublicKey(publicKey);
        Signature signature = Signature.getInstance(signAlgorithm);
        signature.initVerify((PublicKey) key);
        signature.update(data);
        return signature.verify(BASE64Util.decode(sign));
    }

    /**
     * 私钥->Key类型(PKCS8)
     * @param privateKey 密钥
     */
    public static Key generateKeyByPrivateKey(String privateKey) {
        try {
            byte[] keyBytes = BASE64Util.decode(privateKey);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 公钥->Key类型(x509)
     * @param publicKey 公钥
     */
    public static Key generateKeyByPublicKey(String publicKey) {
        try {
            byte[] keyBytes = BASE64Util.decode(publicKey);
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            return keyFactory.generatePublic(x509KeySpec);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
