package com.encryption.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


/**
 *
 * 算法模式: ECB
 * 补码方式: PKCS5Padding
 * 数据块: 默认128位
 * 加解密串编码: Base64 (相当于2次加密)
 * 字符集: utf-8
 */
public enum AESUtil {
    ;

    private static final String AES = "AES";


    /**
     * @Description: 加密
     * @date 2020/5/6 16:52
     * @param src 目标字符串
     * @param encryptionKey 密钥
     * @throws
     */
    public static String encrypt(String src, String encryptionKey) {
        try {
            // "算法/模式/补码方式"
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            Key secretKey = makeSecretKey(encryptionKey);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(src.getBytes(StandardCharsets.UTF_8));
            // 此处使用BASE64做转码功能，同时能起到2次加密的作用。
            return new BASE64Encoder().encodeBuffer(encrypted);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description: 解密
     * @date 2020/5/6 16:52
     * @param src 目标字符串
     * @param encryptionKey 密钥
     */
    public static String decrypt(String src, String encryptionKey) {
        String decrypted = "";
        try {
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.DECRYPT_MODE, makeSecretKey(encryptionKey));
            BASE64Decoder base64Decoder = new BASE64Decoder();
            decrypted = new String(cipher.doFinal(base64Decoder.decodeBuffer(src)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return decrypted;
    }

    /**
     * 将密钥转换成Key类型
     * @param encryptionKey 密钥
     * @return
     */
    public static Key makeSecretKey(String encryptionKey) {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            // 默认也是 SHA1
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            // 可选 128,192,256
            generator.init(128, random);
            byte[] keyBytes = encryptionKey.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec key = new SecretKeySpec(keyBytes, AES);
            return key;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成数据块长度为32的AES密钥
     * @return
     */
    public static String genKey() {
        return genKey(128);
    }

    /**
     * 生成AES密钥
     * @param blockLength 可选 128,192,256
     * @return
     */
    public static String genKey(int blockLength) {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            // 默认也是 SHA1
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            // 可选 128,192,256
            generator.init(blockLength, random);
            SecretKey secretKey = generator.generateKey();
            byte[] encoded = secretKey.getEncoded();
            String encodeBuffer = HexUtil.parseByte2HexStr(encoded);
            return encodeBuffer;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        System.out.println(genKey(128));
        System.out.println(genKey(192));
        System.out.println(genKey(256));
    }

}
