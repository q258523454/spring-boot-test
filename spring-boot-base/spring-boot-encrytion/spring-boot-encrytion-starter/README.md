## 介绍  
 这是一款专门针对Java开发的SDK；涵盖了几乎所有加解密算法，包括：AES、DES、RSA、国密算法；  
 让Spring加解密更方便，同样适用于上云应用
 功能一：单独作为工具类使用：简化版本AES、RSA、国密算法 工具类；对国密算法二次封装，使用简单方便  
 功能二：作为Spring Starter；通过配置或注解的方式，直接对spring controller层加解密；自动装配，插拔开关，对代码零侵入  
 开启该功能后，无需记忆配置名，会自动提示所有配置属性，简单方便；
------------  

按Java常用处理，密钥、公钥、私钥、密文、签名 全部用BASE64转码 

------------
  
## 加密解密支持  
- 可进行加密的方式有：
    - - [x] MD5
    - - [x] SHA
    - - [x] AES
    - - [x] DES
    - - [x] RSA 
    - - [ ] 国密算法SM2 (非对称算法)
    - - [ ] 国密算法SM3 (摘要算法)
    - - [ ] 国密算法SM4 (对称算法+摘要算法)
- 可进行解密的方式有：
    - - [x] AES
    - - [x] DES
    - - [x] RSA 
    - - [ ] 国密算法SM2 (非对称算法)
    - - [ ] 国密算法SM4 (对称算法)
## 使用方法
#### 第一步:引入依赖
- 在`pom.xml`中引入依赖：
```xml
   <dependency>
        <groupId>LJ08</groupId>
        <artifactId>encrypt</artifactId>
        <version>2.0.0-RELEASE</version>
    </dependency>
```

#### 第二步:参数配置
在项目的`application.yml`或`application.properties`文件中进行参数配置，例如：
```yaml
# starter注解, 会自动提示
lj08:
  encrypt:
    open: true                                # 当且仅当该配置存在,且open:true 才会注入工具bean,对代码零侵入
    debug: true                               # 是否开启日志
    body-encoding: 'UTF-8'                    # 指定 body 串的字符编码格式, 与加解密的编码无关
    aes:
      key: 'c407284e6249948fa80c040e5859a135' # 全局配置密钥, 注意:优先使用注解配置的key
      mode: 'CBC'                             # 算法模式,AES 默认 ECB
      padding: 'PKCS5Padding'                 # 补码方式,AES 默认 PKCS5Padding
      block-size: 128                         # 密钥数据块大小 128,192,256, 默认128
      charset: 'UTF-8'                        # 字符编码, 默认 UTF-8
      # 偏移量/盐值设置
      iv:
        salt: '0123456789123456'              # 盐值, 计算会转字节,字节salt[offset,offset+len]
        offset: 0                             # 偏移量,默认0
        len: 16                               # 从偏移量开始,取字节数目大小,默认16个字节长度
    des:
      key: 'Bw1/ECrpwmQ='
    rsa:
      # 注意这里一定要用双引号,因为有换行符'\'
      public-key: "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCkvcCFKMoOAwG9mSnGOAZqn+2AZBbmLHKk9YSq\
                   llwrpzRHxRlOSTQ1HemRulF8YZNrRpb9YLmPLmDKc4PYiU9U3ZHZnEPAhbwm2vf0XTYG16jlLgQq\
                   L8YT2zJ2j69kgbEcPBvEenRTbxGKCtdopRZR93/KsfgO7j+sCAxkpcSbaQIDAQAB"
      private-key: "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKS9wIUoyg4DAb2ZKcY4Bmqf7YBk\
                    FuYscqT1hKqWXCunNEfFGU5JNDUd6ZG6UXxhk2tGlv1guY8uYMpzg9iJT1TdkdmcQ8CFvCba9/Rd\
                    NgbXqOUuBCovxhPbMnaPr2SBsRw8G8R6dFNvEYoK12ilFlH3f8qx+A7uP6wIDGSlxJtpAgMBAAEC\
                    gYBdnkOYqHxiiAzmLPMTZOUolKfILZjhxuKDoc55jqbkW8mW+4gM+AbGJLCGEwaZk23OKbhGV0A8\
                    ANQaWJjVZfqBS1KfpDJkJAVIepBSR7TdAh6aB0jS0zpzdTsR4ZQBKJhkTRiAOpg15F0Xhllb+yq4\
                    3kpe7B8JEGbFIOrZtkhIgQJBANEYCc7G2fW6lCLrbrxuEAEhHIM3WfmwkFMVAr11l7nASoc57Fe3\
                    nVbVqFJJcwlnpYRcwgNjJkw/rFm80WjuzpECQQDJspplCmih07GxdxgUnC/lt54QdtXcdHomtAII\
                    /y6jik+vjm+HqWnMduMQrVfBUXqPT5FQwwYK7edA73t5XZtZAkEAsgtTBqTiodBfJaDt6TubGysT\
                    uVPfpLLJIkXI0IUaTkxD6gPTkRnDu1YxfugWMZL1KNFHT1UVHmY1nzAd7Mk0UQJAQPGfQRV+50xw\
                    lMHSISDBU2gyHv8EkX13r2qalScFb2cjjVWBl972z/0f08jczsRbgDWQhr6k/XPo2EHEMinv0QJB\
                    AKYUlxGKni46f7V717swYh3sd5qprOFCRp9O2kBTeiV+ZLWGBOBg74ngWPX6FwgAxXNyoNtwsa2s\
                    KrAL1Z8uoZw="
```

#### 第三步:对控制器响应体进行加解密
##### 静态工具类使用
```
com.zhang.encryptbody.util.AESUtil
com.zhang.encryptbody.util.DESUtil
com.zhang.encryptbody.util.DigestUtil
com.zhang.encryptbody.util.RSAUtil
```
##### 启动类使用-AES
```java
/**
 * @author zj
 * @date 2020-05-08 17:23
 */
@RestController
//@DecryptBody(value = DecryptEnum.AES)
public class AESController {
    private static final Logger logger = LoggerFactory.getLogger(AESController.class);

    /**
     * 密钥的使用优先级:
     * 1.优先使用注解配置
     *   1.1.优先选择方法上的注解@EncryptBody上的密钥
     *   1.2.其次选择类上的注解@EncryptBody上的密钥
     * 2.当没有注解或注解的配置为空的时候, 使用yml配置文件的密钥
     * @param s 待加密字符
     * @return 加密串
     */
    @PostMapping(value = "/aes/encrypt")
    @ResponseBody
    @AESEncryptBody(key = "zIdivNmX09RKO0aiutQ8nQ==")
    public String encrypt(HttpServletRequest request, @RequestBody String s) {
        return s;
    }

    // 解密串必须是 @RequestBody
    @GetMapping(value = "/aes/decrypt")
    @AESDecryptBody(key = "zIdivNmX09RKO0aiutQ8nQ==")
    public String decrypt(HttpServletRequest request, @RequestBody String s) {
        logger.info("body:" + s);
        BodyObject bodyObject = JSONObject.parseObject(s, BodyObject.class);
        logger.info("student is:" + JSON.toJSONString(bodyObject));
        return s;
    }

    @PostMapping(value = "/aes/encrypt2")
    @ResponseBody
    @AESEncryptBody
    public String encrypt2(HttpServletRequest request, @RequestBody String s) {
        return s;
    }

    @GetMapping(value = "/aes/decrypt2")
    @AESDecryptBody
    public String decrypt2(HttpServletRequest request, @RequestBody String s) {
        logger.info("body:" + s);
        BodyObject bodyObject = JSONObject.parseObject(s, BodyObject.class);
        logger.info("student is:" + JSON.toJSONString(bodyObject));
        return s;
    }
}
```
##### 启动类使用-DES
```java
/**
 * @author zj
 * @date 2020-05-08 17:23
 */
@RestController
public class RSAController {
    private static final Logger logger = LoggerFactory.getLogger(RSAController.class);

    @PostMapping(value = "/rsa/encrypt")
    @ResponseBody
    @RSAEncryptBody
    public String encrypt(HttpServletRequest request, @RequestBody String s) {
        return s;
    }

    @GetMapping(value = "/rsa/decrypt")
    @RSADecryptBody
    public String decrypt(HttpServletRequest request, @RequestBody String s) {
        logger.info("body:" + s);
        BodyObject bodyObject = JSONObject.parseObject(s, BodyObject.class);
        logger.info("student is:" + JSON.toJSONString(bodyObject));
        return s;
    }
}
```
##### 启动类使用-摘要算法(MD5,SHA)
```java
@RestController
public class MD5Controller {
    private static final Logger logger = LoggerFactory.getLogger(MD5Controller.class);

    @GetMapping(value = "/md5/encrypt")
    @MD5EncryptBody
    //@SHAEncryptBody
    public String md5() {
        return "API市场";
    }
}
```
