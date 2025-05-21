package com.myjetcachekryo.config.codex;

import com.alicp.jetcache.support.AbstractValueEncoder;
import com.alicp.jetcache.support.CacheEncodeException;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;

import java.lang.ref.WeakReference;
import java.text.MessageFormat;

/**
 * 参考: {@link com.alicp.jetcache.support.KryoValueEncoder}
 */
public class KryoValueProEncoder extends AbstractValueEncoder {
    public static final int INIT_BUFFER_SIZE = 256;

    public static final ThreadLocal<Object[]> KRYO_THREAD_LOCAL = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.setDefaultSerializer(CompatibleFieldSerializer.class);
        // Kryo 5.x 默认修改为 true，不设置为false 会出现 CacheValueHolder 未注册错误
        kryo.setRegistrationRequired(false);
        kryo.setReferences(true);
        // 解决 Class is not registered: com.alicp.jetcache.CacheValueHolder
        // kryo.register(CacheValueHolder.class);
        byte[] buffer = new byte[INIT_BUFFER_SIZE];
        WeakReference<byte[]> ref = new WeakReference<>(buffer);
        return new Object[]{kryo, ref};
    });


    public KryoValueProEncoder(boolean useIdentityNumber) {
        super(useIdentityNumber);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public byte[] apply(Object value) {
        try {
            Object[] kryoAndBuffer = KRYO_THREAD_LOCAL.get();
            Kryo kryo = (Kryo) kryoAndBuffer[0];
            WeakReference<byte[]> ref = (WeakReference<byte[]>) kryoAndBuffer[1];
            byte[] buffer = ref.get();
            if (buffer == null) {
                buffer = new byte[INIT_BUFFER_SIZE];
            }
            Output output = new Output(buffer, -1);

            try {
                if (useIdentityNumber) {
                    writeInt(output, KryoValueProDecoder.IDENTITY_NUMBER_KRYO_PRO);
                }
                kryo.writeClassAndObject(output, value);
                return output.toBytes();
            } finally {
                // reuse buffer if possible
                if (ref.get() == null || buffer != output.getBuffer()) {
                    ref = new WeakReference<>(output.getBuffer());
                    kryoAndBuffer[1] = ref;
                }
            }
        } catch (Exception e) {
            String msg = MessageFormat.format("Kryo Encode error. msg={0}", e.getMessage());
            throw new CacheEncodeException(msg, e);
        }
    }

    /**
     * 解码器唯一标识,4个字节
     * useIdentityNumber=true的时候,解码的时候就用会使用下面的方法提取解码器标识, 获得对应的解码器
     * {@link com.alicp.jetcache.support.AbstractValueDecoder#parseHeader(byte[])}
     */
    private void writeInt(Output output, int value) {
        // kryo5 change writeInt to little endian, so we write int manually
        output.writeByte(value >>> 24);
        output.writeByte(value >>> 16);
        output.writeByte(value >>> 8);
        output.writeByte(value);
    }

}
