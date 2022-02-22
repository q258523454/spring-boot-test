package com.zhang.encryptbody.pojo.properties;


import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Data
public class DesProperties {

    /**
     *  密钥
     */
    private String key = "";

    /**
     * DES 算法模式,默认 ECB
     */
    private String mode = "ECB";

    /**
     * 补码方式,默认 PKCS5Padding
     */
    private String padding = "PKCS5Padding";

    /**
     * 密钥数据块大小 128,192,256, 默认128
     */
    private int blockSize = 128;

    /**
     * 传输报文,盐值偏移量 字符编码, 默认 UTF-8
     */
    private String charset = "UTF-8";

    /**
     * 盐值偏移量
     */
    @NestedConfigurationProperty
    private IvParameter iv;
}
