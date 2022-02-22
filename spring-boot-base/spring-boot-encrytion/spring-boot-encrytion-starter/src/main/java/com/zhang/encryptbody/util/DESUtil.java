package com.zhang.encryptbody.util;

import com.zhang.encryptbody.enums.ModeEnum;
import com.zhang.encryptbody.enums.PaddingEnum;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


/**
 * DES加密算法实现
 * DES全称为Data Encryption Standard，即数据加密标准，是一种使用密钥加密的块算法
 * Java中默认实现为：ECB/PKCS5Padding
 */
@Slf4j
public enum DESUtil {
    ;

    private static final String DES = "DES";

    /**
     * DES加密
     * 算法模式: ECB
     * 补码方式: PKCS5Padding
     * 数据块: 默认16字节(128位)
     * 加解密串编码: Base64 (相当于2次加密)
     * 字符集: utf-8
     * @param data 目标字符串
     * @param desKey 密钥/加密盐值
     */
    public static String encrypt(byte[] data, String desKey) {
        return encrypt(data, desKey, ModeEnum.ECB.getCode(), PaddingEnum.PKCS5Padding.getCode(), null);
    }


    /**
     * DES 加密
     * @param data 目标字符串
     * @param desKey 密钥
     * @param mode 算法模式
     * @param padding 补码方式
     * @param iv  盐值偏移量 ECB模式无需iv
     */
    public static String encrypt(byte[] data, String desKey, String mode, String padding, IvParameterSpec iv) {
        try {
            // "算法/模式/补码方式"
            // eg: method="DES/ECB/PKCS5Padding"
            Cipher cipher = Cipher.getInstance(DES + "/" + mode + "/" + padding);
            cipher.init(Cipher.ENCRYPT_MODE, convertToSecretKey(desKey), iv);
            byte[] encrypted = cipher.doFinal(data);
            // 此处使用BASE64做转码功能，同时能起到2次加密的作用。
            return BASE64Util.encode(encrypted);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * DES 加密
     * @param data 目标字符串
     * @param desKey 密钥
     * @param mode 算法模式
     * @param padding 补码方式
     * @param salt  IvParameterSpec-盐值,ECB模式无需iv
     * @param offset IvParameterSpec-偏移量,ECB模式无需iv
     * @param len   IvParameterSpec-从偏移量开始,取字节数目大小,ECB模式无需iv
     */
    public static String encrypt(byte[] data, String desKey, String mode, String padding,
                                 byte[] salt, int offset, int len) {
        try {
            // "算法/模式/补码方式"
            // eg: method="DES/ECB/PKCS5Padding"
            Cipher cipher = Cipher.getInstance(DES + "/" + mode + "/" + padding);
            Key secretKey = convertToSecretKey(desKey);
            // 盐值偏移量 salt[offset,offset+len]
            IvParameterSpec iv = new IvParameterSpec(salt, offset, len);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] encrypted = cipher.doFinal(data);
            // 此处使用BASE64做转码功能，同时能起到2次加密的作用。
            return BASE64Util.encode(encrypted);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * DES解密, 默认 DES/ECB/PKCS5Padding
     * @param src 待处理BASE64字符串
     * @param desKey 解密密钥
     */
    public static String decrypt(String src, String desKey) {
        // 默认模式:"DES/ECB/PKCS5Padding",ECB模式无需iv
        return decrypt(src, desKey, ModeEnum.ECB.getCode(), PaddingEnum.PKCS5Padding.getCode(), null);
    }

    /**
     * DES-解密
     * @param src 待处理BASE64字符串
     * @param desKey 密钥
     * @param mode 算法模式
     * @param padding 补码方式
     * @param iv IvParameterSpec ECB模式无需iv
     */
    public static String decrypt(String src, String desKey, String mode, String padding, IvParameterSpec iv) {
        String decrypted = "";
        try {
            // "算法/模式/补码方式"
            // eg: method="DES/ECB/PKCS5Padding"
            Cipher cipher = Cipher.getInstance(DES + "/" + mode + "/" + padding);
            cipher.init(Cipher.DECRYPT_MODE, convertToSecretKey(desKey), iv);
            decrypted = new String(cipher.doFinal(BASE64Util.decode(src)));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return decrypted;
    }

    /**
     * DES-解密
     * @param src 待处理BASE64字符串
     * @param desKey 密钥
     * @param mode 算法模式
     * @param padding 补码方式
     * @param salt  IvParameterSpec-盐值, salt[offset,offset+len],ECB模式无需iv
     * @param offset IvParameterSpec-偏移量,ECB模式无需iv
     * @param len   IvParameterSpec-偏移量开始,取字节数目大小,ECB模式无需iv
     */
    public static String decrypt(String src, String desKey, String mode, String padding,
                                 byte[] salt, int offset, int len) {
        String decrypted = "";
        try {
            // "算法/模式/补码方式"
            // eg: method="DES/ECB/PKCS5Padding"
            Cipher cipher = Cipher.getInstance(DES + "/" + mode + "/" + padding);
            // 盐值偏移量 salt[offset,offset+len]
            IvParameterSpec iv = new IvParameterSpec(salt, offset, len);
            cipher.init(Cipher.DECRYPT_MODE, convertToSecretKey(desKey), iv);
            decrypted = new String(cipher.doFinal(BASE64Util.decode(src)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return decrypted;
    }

    /**
     * DES 将密钥盐值转换成Key类型
     * @param desKey 密钥/盐值
     */
    public static Key convertToSecretKey(String desKey) {
        try {
            byte[] bytes = BASE64Util.decode(desKey);
            return new SecretKeySpec(bytes, DES);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * DES 生成密钥
     * 默认为算法:SHA1PRNG, 块大小:16字节(128bit)
     */
    public static String genKey() {
        return genKey("SHA1PRNG", 56);
    }

    /**
     *
     * DES 按指定密钥[数据块长度]生成密钥
     * @param algorithm 密钥算法
     * @param blockLength 可选 56,128,192,256
     */
    public static String genKey(String algorithm, int blockLength) {
        try {
            KeyGenerator generator = KeyGenerator.getInstance(DES);
            // 默认也是 SHA1
            SecureRandom random = SecureRandom.getInstance(algorithm);
            // 可选 128,192,256
            generator.init(blockLength, random);
            SecretKey key = generator.generateKey();
            byte[] keyBytes = key.getEncoded();
            return BASE64Util.encode(keyBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("AES生成密钥报错.");
        }
    }
}
