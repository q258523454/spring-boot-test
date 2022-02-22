package com.zhang.encryptbody.annotation.encrypt;

import com.zhang.encryptbody.enums.DigestAlgorithmEnum;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zj
 * @date 2020/5/11 14:15
 */
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SHAEncryptBody {
    /**
     * 默认 SHA256
     */
    DigestAlgorithmEnum key() default DigestAlgorithmEnum.SHA256;
}
