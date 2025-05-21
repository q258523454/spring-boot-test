package com.zhang.encryptbody.util;

import com.zhang.encryptbody.enums.ModeEnum;
import com.zhang.encryptbody.enums.PaddingEnum;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


/**
 * AES加密算法实现<br>
 * 高级加密标准（英语：Advanced Encryption Standard，缩写：AES），在密码学中又称Rijndael加密法<br>
 * 对于Java中AES的默认模式是：AES/ECB/PKCS5Padding
 * 相关概念说明：
 * mode:    加密算法模式，是用来描述加密算法（此处特指分组密码，不包括流密码，）在加密时对明文分组的模式，它代表了不同的分组方式
 * padding: 补码方式是在分组密码中，当明文长度不是分组长度的整数倍时，需要在最后一个分组中填充一些数据使其凑满一个分组的长度。
 * iv:      在对明文分组加密时，会将明文分组与前一个密文分组进行XOR运算（即异或运算），但是加密第一个明文分组时不存在“前一个密文分组”，
 *          因此需要事先准备一个与分组长度相等的比特序列来代替，这个比特序列就是偏移量。
 * 相关概念见：https://blog.csdn.net/OrangeJack/article/details/82913804
 */
public enum AESUtil {
    ;

    private static final String AES = "AES";


    /**
     * 生成密钥
     * 默认为算法:SHA1PRNG, 块大小:16字节(128bit)
     */
    public static String genKey() {
        return genKey("SHA1PRNG", 128);
    }

    /**
     * 生成密钥
     * 默认为算法:SHA1PRNG
     */
    public static String genKey(int blockLength) {
        return genKey("SHA1PRNG", blockLength);
    }

    /**
     * 按指定密钥[数据块长度]生成密钥
     * @param algorithm 密钥算法
     * @param blockLength 可选 128,192,256
     */
    public static String genKey(String algorithm, int blockLength) {
        try {
            KeyGenerator generator = KeyGenerator.getInstance(AES);
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

    /**
     * 加密
     * 算法模式: ECB
     * 补码方式: PKCS5Padding
     * 数据块: 默认16字节(128位)
     * 加解密串编码: Base64 (相当于2次加密)
     * 字符集: utf-8
     * @param data 目标字符串
     * @param aesKey 密钥
     */
    public static String encrypt(byte[] data, String aesKey) {
        // 默认模式:ECB/PKCS5Padding, ECB模式无需iv
        return encrypt(data, aesKey, ModeEnum.ECB.getCode(), PaddingEnum.PKCS5Padding.getCode(), null);
    }

    /**
     * AES加密方法
     * @param data 目标字符串
     * @param aesKey 密钥
     * @param mode 算法模式
     * @param padding 补码方式
     * @param iv  盐值偏移量,ECB模式无需iv
     */
    public static String encrypt(byte[] data, String aesKey, String mode, String padding, IvParameterSpec iv) {
        try {
            // "算法/模式/补码方式"
            // eg: method="AES/ECB/PKCS5Padding"
            Cipher cipher = Cipher.getInstance(AES + "/" + mode + "/" + padding);
            Key secretKey = convertToSecretKey(aesKey);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] encrypted = cipher.doFinal(data);
            // 此处使用BASE64做转码功能，同时能起到2次加密的作用。
            return BASE64Util.encode(encrypted);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * AES加密方法
     * @param data 目标字符串
     * @param aesKey 密钥
     * @param mode 算法模式
     * @param padding 补码方式
     * @param salt IvParameterSpec-盐值,ECB模式无需iv
     * @param offset IvParameterSpec-偏移量,ECB模式无需iv
     * @param len IvParameterSpec-从偏移量开始,取字节数目大小,ECB模式无需iv
     */
    public static String encrypt(byte[] data, String aesKey, String mode, String padding,
                                 byte[] salt, int offset, int len) {
        try {
            // "算法/模式/补码方式"
            // eg: method="AES/ECB/PKCS5Padding"
            Cipher cipher = Cipher.getInstance(AES + "/" + mode + "/" + padding);
            Key secretKey = convertToSecretKey(aesKey);
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
     * AES-解密
     * @param src 待处理BASE64字符串
     * @param aesKey 解密密钥
     */
    public static String decrypt(String src, String aesKey) {
        // 默认模式:ECB/PKCS5Padding
        return decrypt(src, aesKey, ModeEnum.ECB.getCode(), PaddingEnum.PKCS5Padding.getCode(), null);
    }

    /**
     * AES-解密
     * @param src 待处理BASE64字符串
     * @param aesKey 密钥
     * @param mode 算法模式
     * @param padding 补码方式
     * @param iv IvParameterSpec ECB模式无需iv
     */
    public static String decrypt(String src, String aesKey, String mode, String padding, IvParameterSpec iv) {
        String decrypted = "";
        try {
            // "算法/模式/补码方式"
            // eg: method="AES/ECB/PKCS5Padding"
            Cipher cipher = Cipher.getInstance(AES + "/" + mode + "/" + padding);
            cipher.init(Cipher.DECRYPT_MODE, convertToSecretKey(aesKey), iv);
            decrypted = new String(cipher.doFinal(BASE64Util.decode(src)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return decrypted;
    }

    /**
     * AES-解密
     * @param src 待处理BASE64字符串
     * @param aesKey 密钥
     * @param mode 算法模式
     * @param padding 补码方式
     * @param salt  IvParameterSpec-盐值, salt[offset,offset+len],ECB模式无需iv
     * @param offset IvParameterSpec-偏移量,ECB模式无需iv
     * @param len   IvParameterSpec-偏移量开始,取字节数目大小,ECB模式无需iv
     */
    public static String decrypt(String src, String aesKey, String mode, String padding,
                                 byte[] salt, int offset, int len) {
        String decrypted = "";
        try {
            // "算法/模式/补码方式"
            // eg: method="AES/ECB/PKCS5Padding"
            Cipher cipher = Cipher.getInstance(AES + "/" + mode + "/" + padding);
            // 盐值偏移量 salt[offset,offset+len]
            IvParameterSpec iv = new IvParameterSpec(salt, offset, len);
            cipher.init(Cipher.DECRYPT_MODE, convertToSecretKey(aesKey), iv);
            decrypted = new String(cipher.doFinal(BASE64Util.decode(src)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return decrypted;
    }

    /**
     * 将密钥转换成Key类型
     * @param aesKey 密钥
     */
    public static Key convertToSecretKey(String aesKey) {
        try {
            byte[] bytes = BASE64Util.decode(aesKey);
            return new SecretKeySpec(bytes, AES);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
