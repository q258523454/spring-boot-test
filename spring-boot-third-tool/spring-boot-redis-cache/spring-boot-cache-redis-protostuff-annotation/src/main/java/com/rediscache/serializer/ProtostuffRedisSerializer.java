package com.rediscache.serializer;


import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class ProtostuffRedisSerializer implements RedisSerializer<Object> {

    private static final Schema schema = RuntimeSchema.getSchema(ProtoDataWrapper.class);

    public byte[] serialize(Object object) throws SerializationException {
        if (object != null) {
            LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
            try {
                // Protostuff 是基于POJO进行序列化和反序列化操作, Protostuff 包装类是为了解决序列化 Map,List 等
                // 完整的工具使用参考: com.redis.util.ProtoStuffUtil
                return ProtostuffIOUtil.toByteArray(new ProtoDataWrapper(object), schema, buffer);
            } finally {
                buffer.clear();
            }
        }
        return null;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Object deserialize(byte[] bytes) throws SerializationException {

        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            ProtoDataWrapper ProtoDataWrapper = new ProtoDataWrapper<>();
            ProtostuffIOUtil.mergeFrom(bytes, ProtoDataWrapper, schema);
            return ProtoDataWrapper.getData();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}