package com.zhang.encryptbody.enums;


public enum RSASignTypeEnum {
    /**
     * RSA 签名算法
     */
    MD2withRSA("MD2withRSA", ""),
    MD5withRSA("MD5withRSA", ""),
    SHA1withRSA("SHA1withRSA", ""),
    SHA256withRSA("SHA256withRSA", ""),
    SHA384withRSA("SHA384withRSA", ""),
    SHA512withRSA("SHA512withRSA", ""),
    ;


    private String code;
    private String desc;


    RSASignTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static RSASignTypeEnum parse(String code) {
        for (RSASignTypeEnum item : values()) {
            if (item.code.equals(code)) {
                return item;
            }
        }
        throw new RuntimeException(code + "不存在");
    }
}
