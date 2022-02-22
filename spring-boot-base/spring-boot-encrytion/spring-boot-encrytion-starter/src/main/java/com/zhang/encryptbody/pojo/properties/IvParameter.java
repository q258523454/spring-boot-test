package com.zhang.encryptbody.pojo.properties;


import lombok.Data;

/**
 * 盐值偏移量设置
 */
@Data
public class IvParameter {

    /**
     * 盐值, 计算会转字节,字节salt[offset,offset+len]
     */
    private String salt = "";

    /**
     * 偏移量,默认0
     */
    private int offset = 0;

    /**
     * 从偏移量开始,取字节数目大小,默认16个字节长度
     */
    private int len = 16;

}
