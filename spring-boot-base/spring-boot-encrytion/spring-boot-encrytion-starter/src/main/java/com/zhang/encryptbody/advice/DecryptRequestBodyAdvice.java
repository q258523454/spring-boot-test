package com.zhang.encryptbody.advice;

import com.alibaba.fastjson.JSON;
import com.zhang.encryptbody.annotation.decrypt.AESDecryptBody;
import com.zhang.encryptbody.annotation.decrypt.DESDecryptBody;
import com.zhang.encryptbody.annotation.decrypt.RSADecryptBody;
import com.zhang.encryptbody.enums.TypeEnum;
import com.zhang.encryptbody.exception.DecryptBodyException;
import com.zhang.encryptbody.pojo.internal.DecryptProperties;
import com.zhang.encryptbody.pojo.properties.AesProperties;
import com.zhang.encryptbody.pojo.properties.DesProperties;
import com.zhang.encryptbody.pojo.properties.EncryptConfig;
import com.zhang.encryptbody.pojo.properties.RsaProperties;
import com.zhang.encryptbody.util.AESUtil;
import com.zhang.encryptbody.util.DESUtil;
import com.zhang.encryptbody.util.RSAUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * 请求数据的加密信息解密处理 <br>
 *     本类只对控制器参数中含有注解
 *     <strong>{@link org.springframework.web.bind.annotation.RequestBody}</strong>
 *     进行的方法拦截.
 *     处理以下注解
 *     {@link com.zhang.encryptbody.annotation.decrypt.AESDecryptBody}
 *     {@link com.zhang.encryptbody.annotation.decrypt.DESDecryptBody}
 *     {@link com.zhang.encryptbody.annotation.decrypt.RSADecryptBody}
 *
 * @see  RequestBodyAdvice
 * @author zj
 * @date 2020 /5/11 14:15
 */
@Slf4j
@ControllerAdvice
public class DecryptRequestBodyAdvice implements RequestBodyAdvice {

    public DecryptRequestBodyAdvice() {
        log.info("DecryptRequestBodyAdvice Constructor");
    }

    @Autowired
    private EncryptConfig config;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 如果配置没有开启, 注解不失效
        if (!config.isOpen()) {
            log.warn("加解密开关-未开启");
            return false;
        }

        // 类注解
        Annotation[] classAnotations = methodParameter.getDeclaringClass().getAnnotations();
        if (null != classAnotations && classAnotations.length > 0) {
            for (Annotation annotation : classAnotations) {
                if (annotation instanceof AESDecryptBody ||
                        annotation instanceof DESDecryptBody ||
                        annotation instanceof RSADecryptBody) {
                    return true;
                }
            }
        }
        // 方法注解
        Annotation[] methodAnnotations = null;
        if (null != methodParameter.getMethod()) {
            methodAnnotations = methodParameter.getMethod().getAnnotations();
        }
        if (null != methodAnnotations && methodAnnotations.length > 0) {
            for (Annotation annotation : methodAnnotations) {
                if (annotation instanceof AESDecryptBody ||
                        annotation instanceof DESDecryptBody ||
                        annotation instanceof RSADecryptBody) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        String body = "";
        try {
            body = IOUtils.toString(inputMessage.getBody(), config.getBodyEncoding());
        } catch (IOException e) {
            throw new DecryptBodyException("Unable to get request body data, check body or request is compatible.");
        }
        if (body == null || StringUtils.isEmpty(body)) {
            throw new DecryptBodyException("The request body is null or empty, Decryption failed.");
        }

        // 替换Base64中可能将+替换成功空格
        body = body.replaceAll(" ", "+");
        if (config.isDebug()) {
            log.info("body before decrypt :" + body);
        }

        String decryptBody = null;
        DecryptProperties methodAnnotation = this.getMethodAnnotation(parameter);
        DecryptProperties classAnnotation = this.getClassAnnotation(parameter.getDeclaringClass());

        long startTime = System.currentTimeMillis();
        // 优先方法上注解, 其次类注解; 配置使用优先级:1.注解; 2.yml配置
        if (methodAnnotation != null) {
            decryptBody = doDecrypt(body, methodAnnotation);
        } else if (classAnnotation != null) {
            decryptBody = doDecrypt(body, classAnnotation);
        }
        long endTime = System.currentTimeMillis();
        if (decryptBody == null) {
            throw new DecryptBodyException("Decryption error, check if the source data is encrypted correctly.");
        }
        if (config.isDebug()) {
            log.info("decrypted time:{},decrypted body:{}", endTime - startTime, decryptBody);
        }

        // 重置 HttpInputMessage中Headers的"Content-Length"值, 因为 Body 内容更改了
        // 下面方法等价于: inputMessage.getHeaders().setContentLength(String.valueOf(decryptBody.length()));
        // 注意:"Content-Length" 值是 byte 的长度
        try {
            InputStream inputStream = IOUtils.toInputStream(decryptBody, config.getBodyEncoding());
            byte[] bodyBytes = decryptBody.getBytes();
            inputMessage.getHeaders().set(HttpHeaders.CONTENT_LENGTH, String.valueOf(bodyBytes.length));
            return new HttpInputMessageImpl(inputStream, inputMessage.getHeaders());
        } catch (IOException e) {
            throw new DecryptBodyException("String convert exception. Please check if the format such as encoding is correct.");
        }
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    /**
     * 获取方法控制器上的加密注解信息
     * @param methodParameter 方法参数
     * @return 加密注解信息
     */
    private DecryptProperties getMethodAnnotation(MethodParameter methodParameter) {
        DESDecryptBody desDecryptBody = methodParameter.getMethodAnnotation(DESDecryptBody.class);
        if (null != desDecryptBody) {
            return DecryptProperties.builder()
                    .type(TypeEnum.DES)
                    .key(desDecryptBody.key())
                    .build();
        }
        AESDecryptBody aesDecryptBody = methodParameter.getMethodAnnotation(AESDecryptBody.class);
        if (null != aesDecryptBody) {
            return DecryptProperties.builder()
                    .type(TypeEnum.AES)
                    .key(aesDecryptBody.key())
                    .build();
        }
        RSADecryptBody rsaDecryptBody = methodParameter.getMethodAnnotation(RSADecryptBody.class);
        if (null != rsaDecryptBody) {
            return DecryptProperties.builder()
                    .type(TypeEnum.RSA)
                    .key(rsaDecryptBody.key())
                    .build();
        }
        return null;
    }

    /**
     * 获取类控制器上的加密注解信息
     * @param clazz 控制器类
     * @return 加密注解信息
     */
    private DecryptProperties getClassAnnotation(Class clazz) {
        Annotation[] annotations = clazz.getDeclaredAnnotations();
        if (annotations != null && annotations.length > 0) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof DESDecryptBody) {
                    return DecryptProperties.builder()
                            .type(TypeEnum.DES)
                            .key(((DESDecryptBody) annotation).key())
                            .build();
                }
                if (annotation instanceof AESDecryptBody) {
                    return DecryptProperties.builder()
                            .type(TypeEnum.AES)
                            .key(((AESDecryptBody) annotation).key())
                            .build();
                }
                if (annotation instanceof RSADecryptBody) {
                    return DecryptProperties.builder()
                            .type(TypeEnum.RSA)
                            .key(((RSADecryptBody) annotation).key())
                            .build();
                }
            }
        }
        return null;
    }


    /**
     * 选择加密方式并进行解密
     * @param inputBody 目标解密字符串
     * @param decryptProperties 加密信息
     * @return 解密结果
     */
    private String doDecrypt(String inputBody, DecryptProperties decryptProperties) {
        TypeEnum type = decryptProperties.getType();
        switch (type) {
            case DES:
                DesProperties desProperties = config.getDes();
                String desKey = KeyUtil.getDesKey(decryptProperties.getKey(), desProperties);
                if (config.isDebug()) {
                    log.info("body encoding is :{}", config.getBodyEncoding());
                    log.info("DES decrypt config is :{}", JSON.toJSONString(config.getDes()));
                    log.info("DES decrypt used key is :{}", desKey);
                }
                if (null != desProperties.getIv()) {
                    return DESUtil.decrypt(inputBody, desKey, desProperties.getMode(), desProperties.getPadding(),
                            desProperties.getIv().getSalt().getBytes(Charset.forName(desProperties.getCharset())),
                            desProperties.getIv().getOffset(),
                            desProperties.getIv().getLen());
                }
                return DESUtil.decrypt(inputBody, desKey, desProperties.getMode(), desProperties.getPadding(), null);
            case AES:
                AesProperties aesProperties = config.getAes();
                String aesKey = KeyUtil.getAesKey(decryptProperties.getKey(), aesProperties);
                if (config.isDebug()) {
                    log.info("body encoding is :{}", config.getBodyEncoding());
                    log.info("AES decrypt config is :{}", JSON.toJSONString(config.getAes()));
                    log.info("AES decrypt used key is :{}", aesKey);
                }
                if (null != aesProperties.getIv()) {
                    return AESUtil.decrypt(inputBody, aesKey, aesProperties.getMode(), aesProperties.getPadding(),
                            aesProperties.getIv().getSalt().getBytes(Charset.forName(aesProperties.getCharset())),
                            aesProperties.getIv().getOffset(),
                            aesProperties.getIv().getLen());
                }
                return AESUtil.decrypt(inputBody, aesKey, aesProperties.getMode(), aesProperties.getPadding(), null);
            case RSA:
                RsaProperties rsaProperties = config.getRsa();
                String rsaDecryptKey = KeyUtil.getRsaDecryptKey(decryptProperties.getKey(), config.getRsa());
                if (config.isDebug()) {
                    log.info("body encoding is :{}", config.getBodyEncoding());
                    log.info("RSA decrypt config is :{}", JSON.toJSONString(config.getRsa()));
                    log.info("RSA decrypt used key is :{}", rsaDecryptKey);
                }
                return RSAUtil.decrypt(inputBody, rsaDecryptKey, rsaProperties.getMode(), rsaProperties.getPadding());

            default:
                throw new DecryptBodyException("不支持该类型解密");
        }
    }
}
