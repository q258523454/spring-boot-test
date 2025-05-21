package com.myjetcachekryo.config.codex;

import com.alicp.jetcache.support.AbstractValueDecoder;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;

import java.io.ByteArrayInputStream;
import java.io.Closeable;


/**
 * 参考: {@link com.alicp.jetcache.support.KryoValueDecoder}
 */
public class KryoValueProDecoder extends AbstractValueDecoder {

    public static final KryoValueProDecoder INSTANCE = new KryoValueProDecoder(true);

    /**
     * 解码器唯一标识 1251293827
     */
    public static final int IDENTITY_NUMBER_KRYO_PRO = 0x4A953A83;

    /**
     * 是否开启解码前缀, 执行decode的时候, 会先执行下面的方法, 里面会判断是否根据 identity number 获取解码器
     * {@link com.alicp.jetcache.support.AbstractValueDecoder#apply(byte[])}
     */
    public KryoValueProDecoder(boolean useIdentityNumber) {
        super(useIdentityNumber);
    }

    @Override
    public Object doApply(byte[] buffer) {
        ByteArrayInputStream in = null;
        try {
            if (useIdentityNumber) {
                in = new ByteArrayInputStream(buffer, 4, buffer.length - 4);
            } else {
                in = new ByteArrayInputStream(buffer);
            }
            Input input = new Input(in);
            Kryo kryo = (Kryo) KryoValueProEncoder.KRYO_THREAD_LOCAL.get()[0];
            ClassLoader classLoader = KryoValueProDecoder.class.getClassLoader();
            ClassLoader ctxClassLoader = Thread.currentThread().getContextClassLoader();
            if (ctxClassLoader != null) {
                classLoader = ctxClassLoader;
            }
            kryo.setClassLoader(classLoader);
            return kryo.readClassAndObject(input);
        } finally {
            close(in);
        }
    }


    // 关闭io流对象
    public void close(Closeable io) {
        if (io != null) {
            try {
                io.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
