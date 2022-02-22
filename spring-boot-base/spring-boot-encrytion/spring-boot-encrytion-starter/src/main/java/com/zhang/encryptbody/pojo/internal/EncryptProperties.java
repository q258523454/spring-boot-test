package com.zhang.encryptbody.pojo.internal;

import com.zhang.encryptbody.enums.DigestAlgorithmEnum;
import com.zhang.encryptbody.enums.TypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>加密注解信息</p>
 * @author zj
 * @date 2020/5/11 14:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EncryptProperties {

    /**
     * 加密方式
     */
    private TypeEnum type;

    /**
     * 加密密钥
     */
    private String key;

    /**
     * 摘要加密算法,适用于摘要算法
     */
    private DigestAlgorithmEnum digest;
}
