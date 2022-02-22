package com.zhang.encryptbody.advice;

import com.alibaba.fastjson.JSON;
import com.zhang.encryptbody.annotation.encrypt.AESEncryptBody;
import com.zhang.encryptbody.annotation.encrypt.DESEncryptBody;
import com.zhang.encryptbody.annotation.encrypt.MD5EncryptBody;
import com.zhang.encryptbody.annotation.encrypt.RSAEncryptBody;
import com.zhang.encryptbody.annotation.encrypt.SHAEncryptBody;
import com.zhang.encryptbody.enums.DigestAlgorithmEnum;
import com.zhang.encryptbody.enums.TypeEnum;
import com.zhang.encryptbody.exception.EncryptBodyException;
import com.zhang.encryptbody.pojo.internal.EncryptProperties;
import com.zhang.encryptbody.pojo.properties.AesProperties;
import com.zhang.encryptbody.pojo.properties.DesProperties;
import com.zhang.encryptbody.pojo.properties.EncryptConfig;
import com.zhang.encryptbody.pojo.properties.RsaProperties;
import com.zhang.encryptbody.util.AESUtil;
import com.zhang.encryptbody.util.DESUtil;
import com.zhang.encryptbody.util.DigestUtil;
import com.zhang.encryptbody.util.RSAUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.annotation.Annotation;
import java.nio.charset.Charset;


/**
 * 响应数据的加密处理 <br>
 *     本类只对控制器参数中含有
 *     <strong>{@link org.springframework.web.bind.annotation.ResponseBody}</strong>
 *     或者类上含有
 *     <strong>{@link org.springframework.web.bind.annotation.RestController}</strong>
 *     的注解进行拦截.
 *     处理以下注解
 *      <strong>{@link com.zhang.encryptbody.annotation.encrypt.SHAEncryptBody}
 *      <strong>{@link com.zhang.encryptbody.annotation.encrypt.RSAEncryptBody}
 *      <strong>{@link com.zhang.encryptbody.annotation.encrypt.MD5EncryptBody}
 *      <strong>{@link com.zhang.encryptbody.annotation.encrypt.DESEncryptBody}
 *      <strong>{@link com.zhang.encryptbody.annotation.encrypt.AESEncryptBody}
 *      <strong>{@link com.zhang.encryptbody.annotation.encrypt.RSAEncryptBody}
 *
 * @see ResponseBodyAdvice
 * @author zj
 * @date 2020/5/11 14:15
 */
@Slf4j
@ControllerAdvice
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    public EncryptResponseBodyAdvice() {
        log.info("EncryptResponseBodyAdvice Constructor");
    }

    @Autowired
    private EncryptConfig config;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        if (!config.isOpen()) {
            log.warn("加解密开关-未开启");
            return false;
        }

        // 类注解
        Annotation[] classAnotations = returnType.getDeclaringClass().getAnnotations();
        if (null != classAnotations && classAnotations.length > 0) {
            for (Annotation annotation : classAnotations) {
                if (annotation instanceof AESEncryptBody ||
                        annotation instanceof DESEncryptBody ||
                        annotation instanceof RSAEncryptBody ||
                        annotation instanceof MD5EncryptBody ||
                        annotation instanceof SHAEncryptBody) {
                    return true;
                }
            }
        }
        // 方法注解
        Annotation[] methodAnnotations = null;
        if (null != returnType.getMethod()) {
            methodAnnotations = returnType.getMethod().getAnnotations();
        }
        if (null != methodAnnotations && methodAnnotations.length > 0) {
            for (Annotation annotation : methodAnnotations) {
                if (annotation instanceof AESEncryptBody ||
                        annotation instanceof DESEncryptBody ||
                        annotation instanceof RSAEncryptBody ||
                        annotation instanceof MD5EncryptBody ||
                        annotation instanceof SHAEncryptBody) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // 只对String类型加解密
        if (!(body instanceof String)) {
            throw new EncryptBodyException("encrypt data is not type [java.lang.String]");
        }
        response.getHeaders().setContentType(MediaType.TEXT_PLAIN);
        String bodyStr = (String) body;
        if (config.isDebug()) {
            log.info("encrypt body is :" + bodyStr);
        }

        String encryptBody = null;
        long startTime = System.currentTimeMillis();
        // 获取类控制器上的加密注解信息
        EncryptProperties classAnnotation = this.getClassAnnotation(returnType.getDeclaringClass());
        EncryptProperties methodAnnotation = this.getMethodAnnotation(returnType);

        // 优先方法上注解, 其次类注解; 配置使用优先级:1.注解; 2.yml配置
        if (methodAnnotation != null) {
            encryptBody = doEncrypt(bodyStr, methodAnnotation);
        } else if (classAnnotation != null) {
            encryptBody = doEncrypt(bodyStr, classAnnotation);
        }

        long endTime = System.currentTimeMillis();
        if (config.isDebug()) {
            log.info("decrypted time:{},decrypted body:{}", endTime - startTime, encryptBody);
        }
        if (null == encryptBody) {
            throw new EncryptBodyException();
        }

        return encryptBody;
    }


    /**
     * 获取类控制器上的加密注解信息
     * @param clazz 控制器类
     * @return 加密注解信息
     */
    private EncryptProperties getClassAnnotation(Class<?> clazz) {
        Annotation[] annotations = clazz.getDeclaredAnnotations();
        if (annotations != null && annotations.length > 0) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof MD5EncryptBody) {
                    return EncryptProperties.builder()
                            .type(TypeEnum.MD5)
                            .build();
                }
                if (annotation instanceof SHAEncryptBody) {
                    return EncryptProperties.builder()
                            .type(TypeEnum.SHA)
                            .key(((SHAEncryptBody) annotation).key().getCode())
                            .build();
                }
                if (annotation instanceof DESEncryptBody) {
                    return EncryptProperties.builder()
                            .type(TypeEnum.DES)
                            .key(((DESEncryptBody) annotation).key())
                            .build();
                }
                if (annotation instanceof AESEncryptBody) {
                    return EncryptProperties.builder()
                            .type(TypeEnum.AES)
                            .key(((AESEncryptBody) annotation).key())
                            .build();
                }
                if (annotation instanceof RSAEncryptBody) {
                    return EncryptProperties.builder()
                            .type(TypeEnum.RSA)
                            .key(((RSAEncryptBody) annotation).key())
                            .build();
                }
            }
        }
        return null;
    }

    /**
     * 获取方法控制器上的加密注解信息
     * @param methodParameter 控制器方法
     * @return 加密注解信息
     */
    private EncryptProperties getMethodAnnotation(MethodParameter methodParameter) {
        MD5EncryptBody md5EncryptBody = methodParameter.getMethodAnnotation(MD5EncryptBody.class);
        if (null != md5EncryptBody) {
            return EncryptProperties.builder()
                    .type(TypeEnum.MD5)
                    .build();
        }
        SHAEncryptBody shaEncryptBody = methodParameter.getMethodAnnotation(SHAEncryptBody.class);
        if (null != shaEncryptBody) {
            return EncryptProperties.builder()
                    .type(TypeEnum.SHA)
                    .key(shaEncryptBody.key().getCode())
                    .build();
        }
        DESEncryptBody desEncryptBody = methodParameter.getMethodAnnotation(DESEncryptBody.class);
        if (null != desEncryptBody) {
            return EncryptProperties.builder()
                    .type(TypeEnum.DES)
                    .key(desEncryptBody.key())
                    .build();
        }
        AESEncryptBody aesEncryptBody = methodParameter.getMethodAnnotation(AESEncryptBody.class);
        if (null != aesEncryptBody) {
            return EncryptProperties.builder()
                    .type(TypeEnum.AES)
                    .key(aesEncryptBody.key())
                    .build();
        }

        RSAEncryptBody rsaEncryptBody = methodParameter.getMethodAnnotation(RSAEncryptBody.class);
        if (null != rsaEncryptBody) {
            return EncryptProperties.builder()
                    .type(TypeEnum.RSA)
                    .key(rsaEncryptBody.key())
                    .build();
        }
        return null;
    }

    /**
     * 选择加密方式并进行加密
     * @param returnBody 目标加密字符串
     * @param encryptProperties 加密信息
     * @return 加密结果
     */
    private String doEncrypt(String returnBody, EncryptProperties encryptProperties) {
        TypeEnum type = encryptProperties.getType();
        byte[] bodyBytes = null;

        switch (type) {
            case MD5:
                return DigestUtil.digest(returnBody, DigestAlgorithmEnum.MD5.getCode());
            case SHA:
                DigestAlgorithmEnum digestAlgorithmEnum = encryptProperties.getDigest();
                return DigestUtil.digest(returnBody, digestAlgorithmEnum.getCode());
            case DES:
                DesProperties desProperties = config.getDes();
                String desKey = KeyUtil.getDesKey(encryptProperties.getKey(), desProperties);
                if (config.isDebug()) {
                    log.info("body encoding is :{}", config.getBodyEncoding());
                    log.info("DES encrypt config is :{}", JSON.toJSONString(config.getDes()));
                    log.info("DES encrypt used key is :{}", desKey);
                }
                bodyBytes = returnBody.getBytes(Charset.forName(desProperties.getCharset()));
                if (null != desProperties.getIv()) {
                    return DESUtil.encrypt(bodyBytes, desKey, desProperties.getMode(), desProperties.getPadding(),
                            desProperties.getIv().getSalt().getBytes(Charset.forName(desProperties.getCharset())),
                            desProperties.getIv().getOffset(),
                            desProperties.getIv().getLen());
                }

                return DESUtil.encrypt(bodyBytes, desKey, desProperties.getMode(), desProperties.getPadding(), null);
            case AES:
                AesProperties aesProperties = config.getAes();
                String aesKey = KeyUtil.getAesKey(encryptProperties.getKey(), config.getAes());
                if (config.isDebug()) {
                    log.info("body encoding is :{}", config.getBodyEncoding());
                    log.info("AES encrypt config is :{}", JSON.toJSONString(config.getAes()));
                    log.info("AES encrypt used key is :{}", aesKey);
                }

                bodyBytes = returnBody.getBytes(Charset.forName(aesProperties.getCharset()));
                byte[] saltBytes = aesProperties.getIv().getSalt().getBytes(Charset.forName(aesProperties.getCharset()));
                if (null != aesProperties.getIv()) {
                    return AESUtil.encrypt(bodyBytes, aesKey, aesProperties.getMode(), aesProperties.getPadding(), saltBytes, aesProperties.getIv().getOffset(), aesProperties.getIv().getLen());
                }
                return AESUtil.encrypt(bodyBytes, aesKey, aesProperties.getMode(), aesProperties.getPadding(), null);
            case RSA:
                RsaProperties rsaProperties = config.getRsa();
                String rsaEncryptKey = KeyUtil.getRsaEncryptKey(encryptProperties.getKey(), config.getRsa());
                if (config.isDebug()) {
                    log.info("body encoding is :{}", config.getBodyEncoding());
                    log.info("RSA encrypt config is :{}", JSON.toJSONString(config.getRsa()));
                    log.info("RSA encrypt used key is :{}", rsaEncryptKey);
                }
                bodyBytes = returnBody.getBytes(Charset.forName(rsaProperties.getCharset()));
                return RSAUtil.encrypt(bodyBytes, rsaEncryptKey, rsaProperties.getMode(), rsaProperties.getPadding());

            default:
                throw new EncryptBodyException("不支持该类型加密");
        }
    }

}
