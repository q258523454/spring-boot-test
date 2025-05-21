package com.zhang.encryptbody.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;


@Slf4j
public class Test_AES_DES {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        aesTestEcb();
        aesTestCbc();

        desTestEcb();
        desTestCbc();

        System.exit(0);
    }


    public static void aesTestEcb() throws IOException {
        String data = "API市场";
        log.info("AES ECB data:{} ", data);

        // 生成密钥
//        String aesKey = AESUtil.genKey("SHA1PRNG", 128);
        String aesKey = "uEiKvygandnKgJKtuc6IYA==";
        log.info("AES ECB Key:{}", aesKey);

        String encrypt = AESUtil.encrypt(data.getBytes(), aesKey);
        log.info("AES ECB encrypt:{}", encrypt);

        String decrypt = AESUtil.decrypt(encrypt, aesKey);
        log.info("AES ECB decrypt:{}", decrypt);
    }

    public static void aesTestCbc() throws IOException {
        String data = "API市场";
        log.info("AES CBC data:{} ", data);

        // 生成密钥
//        String aesKey = AESUtil.genKey("SHA1PRNG", 128);
        String aesKey = "zIdivNmX09RKO0aiutQ8nQ==";
        log.info("AES CBC Key:{}", aesKey);

        IvParameterSpec ivParameterSpec = new IvParameterSpec("0123456789123456".getBytes(), 0, 16);
        String encrypt = AESUtil.encrypt(data.getBytes(), aesKey, "CBC", "PKCS5Padding", ivParameterSpec);
        log.info("AES CBC encrypt:{}", encrypt);

        String decrypt = AESUtil.decrypt(encrypt, aesKey, "CBC", "PKCS5Padding", ivParameterSpec);
        log.info("AES CBC decrypt:{}", decrypt);
    }

    public static void desTestEcb() {
        String data = "zhangxiaofan";
        log.info("DES ECB data:{} ", data);

//        String desKey = DESUtil.genKey();
        String desKey = "Bw1/ECrpwmQ=";
        log.info("DES ECB desKey:{} ", desKey);

        String encrypt = DESUtil.encrypt(data.getBytes(), desKey);
        log.info("DES ECB encrypt:{} ", encrypt);

        String decrypt = DESUtil.decrypt(encrypt, desKey);
        log.info("DES ECB decrypt:{} ", decrypt);
    }

    public static void desTestCbc() {
        String data = "zhangxiaofan";
        log.info("DES CBC data:{} ", data);

//        String desKey = DESUtil.genKey();
        String desKey = "I4yJNwSbufQ=";
        log.info("DES CBC desKey:{} ", desKey);

        IvParameterSpec ivParameterSpec = new IvParameterSpec("12345678".getBytes(), 0, 8);

        String encrypt = DESUtil.encrypt(data.getBytes(), desKey, "CBC", "PKCS5Padding", ivParameterSpec);
        log.info("DES CBC encrypt:{} ", encrypt);

        String decrypt = DESUtil.decrypt(encrypt, desKey, "CBC", "PKCS5Padding", ivParameterSpec);
        log.info("DES CBC decrypt:{} ", decrypt);
    }
}
