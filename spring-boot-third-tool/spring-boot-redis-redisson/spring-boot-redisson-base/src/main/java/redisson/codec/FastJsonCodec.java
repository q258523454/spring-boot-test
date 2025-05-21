package redisson.codec;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import org.redisson.client.codec.BaseCodec;
import org.redisson.client.handler.State;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.Encoder;

import java.io.IOException;

public class FastJsonCodec extends BaseCodec {

    private static final Schema SCHEMA = RuntimeSchema.getSchema(ProtoStuffDataWrapper.class);

    @Override
    public Decoder<Object> getValueDecoder() {
        return decoder;
    }

    @Override
    public Encoder getValueEncoder() {
        return encoder;
    }

    private final Encoder encoder = new Encoder() {
        @Override
        public ByteBuf encode(Object in) throws IOException {
            ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
            try {
                ByteBufOutputStream os = new ByteBufOutputStream(out);
                JSON.writeJSONString(os, in, SerializerFeature.WriteClassName);
                return os.buffer();
            } catch (IOException e) {
                // ByteBuf 只有在出现异常的时候才需要主动释放,因为否则重复释放会报错: IllegalReferenceCountException: refCnt: 0
                out.release();
                throw e;
            } catch (Exception e) {
                out.release();
                throw new IOException(e);
            }
        }
    };

    private final Decoder<Object> decoder = new Decoder<Object>() {
        public Object decode(ByteBuf buf, State state) throws IOException {
            // 添加待序列化对象的扫描包，如果不添加扫描包，无法实现序列化和反序列化
            // 另外待序列化的对象必须有一个默认的构造器，否则也无法实现序列化和反序列化。
            ParserConfig.getGlobalInstance().addAccept("redisson.entity");
            return JSON.parseObject(new ByteBufInputStream(buf), Object.class);
        }
    };
}
