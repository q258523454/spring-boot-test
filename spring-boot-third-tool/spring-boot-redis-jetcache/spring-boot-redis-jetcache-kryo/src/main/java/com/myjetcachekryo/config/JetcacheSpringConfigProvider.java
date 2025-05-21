package com.myjetcachekryo.config;

import com.alicp.jetcache.anno.support.SpringConfigProvider;
import com.alicp.jetcache.support.DecoderMap;
import com.myjetcachekryo.config.convertor.GsonKeyConvertor;
import com.myjetcachekryo.config.codex.KryoValueProDecoder;
import com.myjetcachekryo.config.codex.KryoValueProEncoder;
import com.myjetcachekryo.config.codex.ProtoStuffValueDecoder;
import com.myjetcachekryo.config.codex.ProtoStuffValueEncoder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class JetcacheSpringConfigProvider {

    /**
     * 也可以直接 extends SpringConfigProvider 来实现
     */
    @Bean
    public SpringConfigProvider springConfigProvider() {
        return new SpringConfigProvider() {
            /**
             * 指定KEY的转换方式
             */
            @Override
            public Function<Object, Object> parseKeyConvertor(String convertor) {
                if ("myGson".equalsIgnoreCase(convertor)) {
                    return GsonKeyConvertor.INSTANCE;
                }
                return super.parseKeyConvertor(convertor);
            }

            /**
             * 解码器
             */
            @Override
            public Function<byte[], Object> parseValueDecoder(String valueDecoder) {
                if (valueDecoder.equalsIgnoreCase("myKryo")) {
                    // 是否将 identity number 放到编码前缀
                    KryoValueProDecoder kryoValueProDecoder = new KryoValueProDecoder(true);
                    DecoderMap.defaultInstance().register(KryoValueProDecoder.IDENTITY_NUMBER_KRYO_PRO, kryoValueProDecoder);
                    return kryoValueProDecoder;
                }
                if (valueDecoder.equalsIgnoreCase("myProtoStuff")) {
                    ProtoStuffValueDecoder protoStuffValueDecoder = new ProtoStuffValueDecoder(true);
                    DecoderMap.defaultInstance().register(ProtoStuffValueDecoder.IDENTITY_NUMBER_PROTOSTUFF, protoStuffValueDecoder);
                    return protoStuffValueDecoder;
                }
                return super.parseValueDecoder(valueDecoder);
            }

            /**
             * 编码器
             */
            @Override
            public Function<Object, byte[]> parseValueEncoder(String valueEncoder) {
                if (valueEncoder.equalsIgnoreCase("myKryo")) {
                    return new KryoValueProEncoder(true);
                }
                if (valueEncoder.equalsIgnoreCase("myProtoStuff")) {
                    return new ProtoStuffValueEncoder(true);
                }
                return super.parseValueEncoder(valueEncoder);
            }
        };
    }

}
