package com.myjetcachekryo.util;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 序列化工具 Created by zhang
 */
public final class ProtoStuffUtil {

    // 缓存schema对象的map
    private static final Map<Class<?>, RuntimeSchema<?>> CACHE_SCHEMA = new ConcurrentHashMap<>();

    private static final Schema<ProtoStuffDataWrapper> WRAPPER_SCHEMA = RuntimeSchema.createFrom(ProtoStuffDataWrapper.class);

    private static final int BUFFER_SIZE = 1024 * 1024;

    private static final Set<Class<?>> WRAPPER_CLASS = new HashSet<>();

    static {
        // 新增需要通过包装类来序列化的 Class 对象
        WRAPPER_CLASS.add(List.class);
        WRAPPER_CLASS.add(ArrayList.class);
        WRAPPER_CLASS.add(LinkedList.class);
        WRAPPER_CLASS.add(Set.class);
        WRAPPER_CLASS.add(Map.class);
        WRAPPER_CLASS.add(HashMap.class);
        WRAPPER_CLASS.add(TreeMap.class);
    }

    /**
     * 根据获取相应类型的schema方法
     */
    @SuppressWarnings({"unchecked"})
    public static <T> Schema<T> getSchema(Class<T> clazz) {
        RuntimeSchema<T> schema = (RuntimeSchema<T>) CACHE_SCHEMA.get(clazz);
        if (schema == null) {
            schema = RuntimeSchema.createFrom(clazz);
            CACHE_SCHEMA.put(clazz, schema);
        }
        return schema;
    }

    /**
     * 序列化-对象装换字节流
     */
    @SuppressWarnings("unchecked")
    public static <T> byte[] serialize(T obj) {
        if (obj == null) {
            throw new RuntimeException(MessageFormat.format("序列化对象异常,[{0}]为空", obj));
        }
        LinkedBuffer buffer = LinkedBuffer.allocate(BUFFER_SIZE);
        byte[] protostuff = null;
        try {
            if (WRAPPER_CLASS.contains(obj.getClass())) {
                ProtoStuffDataWrapper<T> protoStuffDataWrapper = new ProtoStuffDataWrapper<>(obj);
                protostuff = ProtostuffIOUtil.toByteArray(protoStuffDataWrapper, getSchema(ProtoStuffDataWrapper.class), buffer);
            } else {
                Schema<T> schema = (Schema<T>) getSchema(obj.getClass());
                protostuff = ProtostuffIOUtil.toByteArray(obj, schema, buffer);
            }
        } catch (Exception e) {
            throw new RuntimeException(MessageFormat.format("序列化异常,类:[{0}],对象:[{1}]:", obj.getClass(), obj), e);
        } finally {
            buffer.clear();
        }
        return protostuff;
    }


    /**
     * 反序列化-字节转换成对象
     */
    public static <T> T deserialize(byte[] paramArrayOfByte, Class<T> targetClass) {
        if (paramArrayOfByte == null || paramArrayOfByte.length == 0) {
            throw new RuntimeException("反序列化对象异常,byte为空");
        }
        T instance = null;
        try {
            if (WRAPPER_CLASS.contains(targetClass)) {
                ProtoStuffDataWrapper<T> obj = new ProtoStuffDataWrapper<>();
                ProtostuffIOUtil.mergeFrom(paramArrayOfByte, obj, WRAPPER_SCHEMA);
                instance = obj.getData();
            } else {
                instance = targetClass.newInstance();
                Schema<T> schema = getSchema(targetClass);
                ProtostuffIOUtil.mergeFrom(paramArrayOfByte, instance, schema);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("反序列化对象异常,无法实例化对象", e);
        }
        return instance;
    }

    /**
     * 序列化List
     */
    public static <T> byte[] serializeList(List<T> objList) {
        if (objList == null || objList.isEmpty()) {
            throw new RuntimeException(MessageFormat.format("序列化列表异常,[{0}]参数异常!", objList));
        }
        @SuppressWarnings("unchecked")
        Schema<T> schema = (Schema<T>) getSchema(objList.get(0).getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(BUFFER_SIZE);
        byte[] protostuff = null;
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            ProtostuffIOUtil.writeListTo(bos, objList, schema, buffer);
            protostuff = bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(MessageFormat.format("序列化列表[{0}]发生异常!", objList), e);
        } finally {
            buffer.clear();
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return protostuff;
    }

    /**
     * 反序列化List
     */
    public static <T> List<T> deserializeList(byte[] paramArrayOfByte, Class<T> targetClass) {
        if (paramArrayOfByte == null || paramArrayOfByte.length == 0) {
            throw new RuntimeException("反序列化列表异常,byte序列为空!");
        }

        Schema<T> schema = getSchema(targetClass);
        List<T> result = null;
        try {
            result = ProtostuffIOUtil.parseListFrom(new ByteArrayInputStream(paramArrayOfByte), schema);
        } catch (IOException e) {
            throw new RuntimeException("反序列化列表异常:", e);
        }
        return result;
    }
}