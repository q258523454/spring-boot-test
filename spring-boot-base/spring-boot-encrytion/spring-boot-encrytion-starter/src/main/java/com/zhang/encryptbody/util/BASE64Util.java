package com.zhang.encryptbody.util;

import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

@Slf4j
public class BASE64Util {
    ;

    /**
     * BASE64Encoder
     */
    private static final BASE64Encoder ENCODER = new BASE64Encoder();

    /**
     * BASE64Decoder
     */
    private static final BASE64Decoder DECODER = new BASE64Decoder();

    /**
     * Decode
     * @param str BASE64字符串
     * @return the byte 字节
     */
    public static byte[] decode(String str) throws IOException {
        return DECODER.decodeBuffer(str);
    }

    /**
     * Encode
     * @param bytes 输入字节
     * @return BASE64字符串
     */
    public static String encode(byte[] bytes) {
        return ENCODER.encodeBuffer(bytes);
    }
}
