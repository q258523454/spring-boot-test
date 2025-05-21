
package com.example.springbootexcel.we;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 填空题类型
 */
@Getter
public enum FillContentTypeEnum {
    /**
     * Owner number character type enum.
     */
    NORMAL_TEXT("normalText", "普通文本"),
    /**
     * Phone number character type enum.
     */
    PHONE("phone", "手机号"),
    /**
     * Identity id number character type enum.
     */
    IDENTITY_ID("identityId", "身份证"),
    /**
     * Qq number number character type enum.
     */
    QQ_NUMBER("QQnumber", "QQ号"),
    /**
     * Mailbox number character type enum.
     */
    MAILBOX("mailbox", "邮箱"),
    /**
     * Telephone number character type enum.
     */
    TELEPHONE("telephone", "电话"),
    ;

    private final String type;

    private final String desc;

    FillContentTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    /**
     * 填写答案时需要加密的类型列表
     *
     * @return 需要加密的类型列表 list
     */
    public static List<String> fillEncryptionType() {
        List<String> list = new ArrayList<>();
        list.add(PHONE.type);
        list.add(IDENTITY_ID.type);
        list.add(MAILBOX.type);
        list.add(TELEPHONE.type);
        return list;
    }

}
