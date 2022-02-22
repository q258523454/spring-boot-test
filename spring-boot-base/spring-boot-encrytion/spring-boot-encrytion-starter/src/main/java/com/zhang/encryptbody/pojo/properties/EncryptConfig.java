package com.zhang.encryptbody.pojo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * <p>加密数据配置读取类</p>
 * <p>在SpringBoot项目中的application.yml中添加配置信息即可</p>
 * <pre>
 *     base:
 *      encrypt:
 *       aes-key: 12345678 # AES加密秘钥
 *       des-key: 12345678 # DES加密秘钥
 * </pre>
 * @author zj
 * @date 2020/5/11 14:15
 */
@Data
@ConfigurationProperties(prefix = "lj08.encrypt")
public class EncryptConfig {

    /**
     * 配置开关, true 的时候才会实例化
     */
    private boolean open = false;

    /**
     * 是否开启日志
     */
    private boolean debug = false;

    /**
     * 指定 body 串的字符编码格式, 与加解密的编码无关。
     */
    private String bodyEncoding = "UTF-8";

    /**
     * AES 配置属性
     */
    @NestedConfigurationProperty
    private AesProperties aes;

    /**
     * DES 配置属性
     */
    @NestedConfigurationProperty
    private DesProperties des;

    /**
     * RSA 配置属性
     */
    @NestedConfigurationProperty
    private RsaProperties rsa;

}
