package redisson.codec;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import lombok.extern.slf4j.Slf4j;

import org.redisson.client.codec.BaseCodec;
import org.redisson.client.handler.State;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.Encoder;

import java.io.IOException;

@Slf4j
public class ProtoStuffCodec extends BaseCodec {

    /**
     * 继承 Redisson Codec 实现后, 因为没有Object.Class信息,因此必须用一个封装类来定义 Schema, 否则无法 prostuff 序列化
     */
    private static final Schema SCHEMA = RuntimeSchema.getSchema(ProtoStuffDataWrapper.class);

    @Override
    public Decoder<Object> getValueDecoder() {
        return decoder;
    }

    @Override
    public Encoder getValueEncoder() {
        return encoder;
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    private final Encoder encoder = new Encoder() {
        @Override
        public ByteBuf encode(Object in) throws IOException {
            if (null == in) {
                throw new NullPointerException();
            }
            LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
            ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
            ByteBufOutputStream os = new ByteBufOutputStream(out);
            try {
                ProtostuffIOUtil.writeTo(os, new ProtoStuffDataWrapper(in), SCHEMA, buffer);
                return os.buffer();
            } catch (Exception e) {
                out.release();
                throw e;
            } finally {
                // 注意:finally 不要调用 release() 释放 ByteBuf, 因为重复释放会报错: io.netty.util.IllegalReferenceCountException: refCnt: 0
                // ProtostuffIOUtil.writeTo() 会调用 ByteBufOutputStream.write() 已经进行'软'置空
                buffer.clear();
            }
        }
    };

    @SuppressWarnings({"unchecked", "rawtypes"})
    private final Decoder<Object> decoder = new Decoder<Object>() {
        @Override
        public Object decode(ByteBuf buf, State state) throws IOException {
            ProtoStuffDataWrapper ProtoStuffDataWrapper = new ProtoStuffDataWrapper<>();
            ProtostuffIOUtil.mergeFrom(new ByteBufInputStream(buf), ProtoStuffDataWrapper, SCHEMA);
            return ProtoStuffDataWrapper.getData();
        }
    };
}
