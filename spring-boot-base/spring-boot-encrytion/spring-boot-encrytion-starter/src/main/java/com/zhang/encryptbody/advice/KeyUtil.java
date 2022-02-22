package com.zhang.encryptbody.advice;

import com.zhang.encryptbody.exception.ConfiguredException;
import com.zhang.encryptbody.exception.KeyNotConfiguredException;
import com.zhang.encryptbody.pojo.properties.AesProperties;
import com.zhang.encryptbody.pojo.properties.DesProperties;
import com.zhang.encryptbody.pojo.properties.RsaProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * @author zj
 * @date 2021-07-23 9:39
 */
@Slf4j
public class KeyUtil {
    ;


    /**
     * 获取 AES key
     * 优先用注解上的,其次用配置中的
     * @param annotationKey 注解上的key
     * @param aesProperties yml配置中的key
     */
    public static String getAesKey(String annotationKey, AesProperties aesProperties) {
        String actualKey = null;
        // 优先选择注解中的密钥/盐值+偏移量
        if (!StringUtils.isEmpty(annotationKey)) {
            actualKey = annotationKey;
        } else {
            // 其次选择配置中的 密钥/盐值+偏移量
            if (null == aesProperties) {
                throw new ConfiguredException("AES config is null");
            }
            if (StringUtils.isEmpty(aesProperties.getKey())) {
                throw new KeyNotConfiguredException("AES key is not configured");
            }
            actualKey = aesProperties.getKey();
        }
        return actualKey;
    }

    /**
     * 获取 DES key
     * 优先用注解上的,其次用配置中的
     * @param annotationKey 注解上的key
     * @param desProperties yml配置中的key
     */
    public static String getDesKey(String annotationKey, DesProperties desProperties) {
        String actualKey = null;
        // 优先选择注解中的密钥/盐值+偏移量
        if (!StringUtils.isEmpty(annotationKey)) {
            actualKey = annotationKey;
        } else {
            // 其次选择配置中的 密钥/盐值+偏移量
            if (null == desProperties) {
                throw new ConfiguredException("DES config is null");
            }
            if (StringUtils.isEmpty(desProperties.getKey())) {
                throw new KeyNotConfiguredException("DES key is not configured");
            }
            actualKey = desProperties.getKey();
        }
        return actualKey;
    }

    /**
     * 获取 RSA 加密密钥, 一般是 public key
     * 优先用注解上的,其次用配置中的
     * @param annotationKey 注解上的key
     * @param rsaProperties yml配置中的key
     */
    public static String getRsaEncryptKey(String annotationKey, RsaProperties rsaProperties) {
        String actualKey = null;
        // 优先选择注解中的密钥/盐值+偏移量
        if (!StringUtils.isEmpty(annotationKey)) {
            actualKey = annotationKey;
        } else {
            // 其次选择配置中的 密钥/盐值+偏移量
            if (null == rsaProperties) {
                throw new ConfiguredException("RSA config is null");
            }
            if (StringUtils.isEmpty(rsaProperties.getPublicKey())) {
                throw new KeyNotConfiguredException("RSA encrypt key is not configured");
            }
            actualKey = rsaProperties.getPublicKey();
        }
        return actualKey;
    }

    /**
     * 获取 RSA 解密密钥, 一般是 private key
     * 优先用注解上的,其次用配置中的
     * @param annotationKey 注解上的key
     * @param rsaProperties yml配置中的key
     */
    public static String getRsaDecryptKey(String annotationKey, RsaProperties rsaProperties) {
        String actualKey = null;
        // 优先选择注解中的密钥/盐值+偏移量
        if (!StringUtils.isEmpty(annotationKey)) {
            actualKey = annotationKey;
        } else {
            // 其次选择配置中的 密钥/盐值+偏移量
            if (null == rsaProperties) {
                throw new ConfiguredException("RSA config is null");
            }
            if (StringUtils.isEmpty(rsaProperties.getPrivateKey())) {
                throw new KeyNotConfiguredException("RSA private key is not configured");
            }
            actualKey = rsaProperties.getPrivateKey();
        }
        return actualKey;
    }
}
