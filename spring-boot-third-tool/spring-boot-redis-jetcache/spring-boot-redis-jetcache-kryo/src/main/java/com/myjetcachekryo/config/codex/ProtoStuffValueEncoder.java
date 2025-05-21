package com.myjetcachekryo.config.codex;

import com.alicp.jetcache.CacheValueHolder;
import com.alicp.jetcache.support.AbstractValueEncoder;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

@Slf4j
public class ProtoStuffValueEncoder extends AbstractValueEncoder {

    @SuppressWarnings({"rawtypes"})
    public static final Schema SCHEMA = RuntimeSchema.getSchema(CacheValueHolder.class);

    /**
     * 预分配长度(字节): 1024KB
     */
    private static final int BUFFER_SIZE = 1024 * 1024;

    public ProtoStuffValueEncoder(boolean useIdentityNumber) {
        super(useIdentityNumber);
    }

    /**
     * 下面的方法会获取返回值,存到缓存中
     * {@link com.alicp.jetcache.redis.RedisCache 中的 do_PUT() 方法}
     */
    @SuppressWarnings({"unchecked"})
    @Override
    public byte[] apply(Object value) {
        // 不能自定义 DataWrapper, 因为Jetcache本身就提供了 CacheValueHolder, 并且value已经被Jetcache封装成了 CacheValueHolder
        // com.alicp.jetcache.redis.RedisCache.do_GET/PUT() 中会直接将字节强制转成 CacheValueHolder
        // CacheValueHolder<V> holder = (CacheValueHolder<V>) valueDecoder.apply(bytes);
        // 因此只能用 Jetcache 提供的 DataWrapper(CacheValueHolder), 自定义的类型会无法转换成 CacheValueHolder<V>
        try {
            LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
            // value 就是 CacheValueHolder  类型,Jetcache 已经做了转换
            byte[] valueBytes = ProtostuffIOUtil.toByteArray(value, SCHEMA, buffer);

            // 是否使用加码器前缀
            if (useIdentityNumber) {
                // 0x4A953A84 解码器唯一标识占 4个字节
                ByteArrayOutputStream bos = new ByteArrayOutputStream(4);
                writeInt(bos, ProtoStuffValueDecoder.IDENTITY_NUMBER_PROTOSTUFF);
                byte[] identityHeadBytes = bos.toByteArray();
                // 解码器4个字节作为整个编码的前缀
                return combine(identityHeadBytes, valueBytes);
            } else {
                return valueBytes;
            }
        } catch (Exception ex) {
            log.error("[ProtoStuffValueEncoder] error:", ex);
            throw ex;
        }
    }

    private void writeInt(ByteArrayOutputStream output, int value) {
        output.write((value >> 24) & 0xFF);
        output.write((value >> 16) & 0xFF);
        output.write((value >> 8) & 0xFF);
        output.write (value & 0xFF);
    }

    private byte[] combine(byte[] bs1, byte[] bs2) {
        byte[] newArray = Arrays.copyOf(bs1, bs1.length + bs2.length);
        System.arraycopy(bs2, 0, newArray, bs1.length, bs2.length);
        return newArray;
    }

}
