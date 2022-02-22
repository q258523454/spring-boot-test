package com.zhang.encryptbody.pojo.internal;

import com.zhang.encryptbody.enums.TypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>解密注解信息</p>
 * @author zj
 * @date 2020/5/11 14:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DecryptProperties {

    /**
     * 加密/解密方式
     */
    private TypeEnum type;

    /**
     * 加密密钥,适用于对称加密算法
     */
    private String key;

}
