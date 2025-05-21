package com.zhang.encryptbody.enums;

/**
 * 模式
 * 加密算法模式，是用来描述加密算法（此处特指分组密码，不包括流密码，）在加密时对明文分组的模式，它代表了不同的分组方式
 */
public enum ModeEnum {
    /**
     * 无模式
     */
    NONE("NONE", "无模式"),
    /**
     * 电子密码本模式（Electronic CodeBook）
     */
    ECB("ECB", "电子密码本模式（Electronic CodeBook）"),
    /**
     * 密码分组连接模式（Cipher Block Chaining）
     */
    CBC("CBC", "密码分组连接模式"),
    /**
     * 密文反馈模式（Cipher Feedback）
     */
    CFB("CFB", "密文反馈模式（Cipher Feedback）"),
    /**
     * 输出反馈模式（Output Feedback）
     */
    OFB("OFB", "输出反馈模式（Output Feedback）"),
    /**
     * 计数器模式（A simplification of OFB）
     */
    CTR("CTR", "计数器模式（A simplification of OFB）"),
    /**
     * Cipher Text Stealing
     */
    CTS("CTS", "Cipher Text Stealing"),
    /**
     * Propagating Cipher Block
     */
    PCBC("PCBC", "Propagating Cipher Block"),
    ;

    private String code;
    private String desc;

    ModeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static ModeEnum parse(String code) {
        for (ModeEnum item : values()) {
            if (item.code.equals(code)) {
                return item;
            }
        }
        throw new RuntimeException(code + "不存在");
    }
}
