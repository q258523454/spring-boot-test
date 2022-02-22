package com.zhang.encryptbody.pojo.properties;


import lombok.Data;

@Data
public class RsaProperties {

    /**
     *  加密密钥, 一般是公钥(public key);
     */
    private String publicKey = "";

    /**
     *  解密密钥, 一般是私钥(private key);
     */
    private String privateKey = "";

    /**
     * AES 算法模式,默认 ECB
     */
    private String mode = "ECB";

    /**
     * 补码方式,默认 PKCS1Padding
     */
    private String padding = "PKCS1Padding";

    /**
     * 传输报文 字符编码, 默认 UTF-8
     */
    private String charset = "UTF-8";
}
