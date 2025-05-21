
package com.myjetcachekryo.config.codex;

import com.alicp.jetcache.CacheValueHolder;
import com.alicp.jetcache.support.AbstractValueDecoder;
import com.dyuproject.protostuff.ProtostuffIOUtil;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;

@Slf4j
public class ProtoStuffValueDecoder extends AbstractValueDecoder {

    /**
     * 解码器唯一标识 1251293828
     */
    public static final int IDENTITY_NUMBER_PROTOSTUFF = 0x4A953A84;

    public ProtoStuffValueDecoder(boolean useIdentityNumber) {
        super(useIdentityNumber);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected Object doApply(byte[] buffer) throws Exception {
        CacheValueHolder instance = new CacheValueHolder<>();
        try {
            if (useIdentityNumber) {
                ByteArrayInputStream in= new ByteArrayInputStream(buffer, 4, buffer.length - 4);
                ProtostuffIOUtil.mergeFrom(in, instance, ProtoStuffValueEncoder.SCHEMA);
            } else {
                ProtostuffIOUtil.mergeFrom(buffer, instance, ProtoStuffValueEncoder.SCHEMA);
            }

        } catch (Exception ex) {
            log.error("[ProtoStuffValueDecoder] error:", ex);
            throw ex;
        }
        return instance;
    }
}
