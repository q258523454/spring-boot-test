package com.encryption.util;

import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public enum DigestUtil {
    /**
     * 实例
     */
    INSTANCE;

    /**
     * 字符集
     */
    private static final String CHAR_SET = "UTF-8";


    /**
     *
     * @param input 加密字符
     * @param algorithm MD5,SHA-256,SHA512等算法
     * @return
     * @throws Exception
     */
    private String getMessageDigest(String input, String algorithm) throws Exception {
        MessageDigest messageDigest = null;
        byte[] digest = null;
        try {
            // 拿到一个MD5转换器（如果想要 SHA-256 参数换成 ”SHA-256”）
            messageDigest = MessageDigest.getInstance(algorithm);
            // 返回字节数组，元素长度固定为16个
            digest = messageDigest.digest(input.getBytes(CHAR_SET));
            // 字符数组转换成字符串返回
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
            throw e;
        }
        return HexUtil.parseByte2HexStr(digest);
    }


    public static void main(String[] args) throws Exception {
        String pwd = "1234";
        // 自定义方法 如果想要 SHA-256 参数换成 ”SHA-256”
        System.out.println(DigestUtil.INSTANCE.getMessageDigest(pwd, "MD5"));
        // 第三方工具方法
        System.out.println(DigestUtils.md5DigestAsHex(pwd.getBytes()));
    }

}
