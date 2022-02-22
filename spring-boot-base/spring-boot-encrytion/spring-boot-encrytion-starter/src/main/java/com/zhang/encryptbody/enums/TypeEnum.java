package com.zhang.encryptbody.enums;

/**
 * <p>加密方式</p>
 * @author zj
 * @date 2020/5/11 14:15
 */
public enum TypeEnum {

    /**
     * 加密方式
     */
    MD5("MD5", ""),
    SHA("SHA", ""),
    DES("DES", ""),
    AES("AES", ""),
    RSA("RSA", ""),
    ;

    private String code;
    private String desc;


    TypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static TypeEnum parse(String code) {
        for (TypeEnum item : values()) {
            if (item.code.equals(code)) {
                return item;
            }
        }
        throw new RuntimeException(code + "不存在");
    }

}
