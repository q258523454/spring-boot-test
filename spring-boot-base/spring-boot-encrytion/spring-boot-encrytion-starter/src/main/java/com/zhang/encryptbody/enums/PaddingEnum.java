package com.zhang.encryptbody.enums;


/**
 * 补码方式
 *
 * <p>
 * 补码方式是在分组密码中，当明文长度不是分组长度的整数倍时，需要在最后一个分组中填充一些数据使其凑满一个分组的长度。
 *
 * @author Looly
 * @see <a href="https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#Cipher"> Cipher章节</a>
 * @since 3.0.8
 */
public enum PaddingEnum {
    /**
     * 无补码
     */
    NoPadding("NoPadding", "无补码"),
    /**
     * 0补码，即不满block长度时使用0填充
     */
    ZeroPadding("ZeroPadding", "0补码，即不满block长度时使用0填充"),
    /**
     * This padding for block ciphers is described in 5.2 Block Encryption Algorithms in the W3C's "XML Encryption Syntax and Processing" document.
     */
    ISO10126Padding("ISO10126Padding", ""),
    /**
     * Optimal Asymmetric Encryption Padding scheme defined in PKCS1
     */
    OAEPPadding("OAEPPadding", ""),
    /**
     * The padding scheme described in PKCS #1("SSL3Padding", ""), used with the RSA algorithm
     */
    PKCS1Padding("PKCS1Padding", ""),
    /**
     * The padding scheme described in RSA Laboratories, "PKCS #5: Password-Based Encryption Standard," version 1.5, November 1993.
     */
    PKCS5Padding("PKCS5Padding", ""),
    /**
     * The padding scheme defined in the SSL Protocol Version 3.0, November 18, 1996, section 5.2.3.2 (CBC block cipher)
     */
    SSL3Padding("SSL3Padding", ""),
    ;

    private String code;
    private String desc;

    PaddingEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static PaddingEnum parse(String code) {
        for (PaddingEnum item : values()) {
            if (item.code.equals(code)) {
                return item;
            }
        }
        throw new RuntimeException(code + "不存在");
    }
}
