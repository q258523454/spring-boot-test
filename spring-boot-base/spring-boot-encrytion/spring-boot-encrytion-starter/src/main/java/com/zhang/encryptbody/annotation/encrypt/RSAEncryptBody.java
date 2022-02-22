package com.zhang.encryptbody.annotation.encrypt;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>加密{@link org.springframework.web.bind.annotation.ResponseBody}响应数据，可用于整个控制类或者某个控制器上</p>
 * @author zj
 * @date 2020/5/11 14:15
 */
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RSAEncryptBody {
    /**
     * 密钥,公私钥. 一般是 公钥(加密),
     */
    String key() default "";

    // TODO 添加签名私钥

}
