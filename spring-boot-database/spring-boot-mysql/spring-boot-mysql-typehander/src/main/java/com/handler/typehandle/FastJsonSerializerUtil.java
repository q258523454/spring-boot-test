package com.handler.typehandle;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

import java.io.IOException;
import java.lang.reflect.Type;


/**
 * FastJson输出格式化：JSON.toJSONString()
 */
public class FastJsonSerializerUtil {

    public static class LongTest implements ObjectSerializer {
        public void write(JSONSerializer jsonSerializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
            Long l = (Long) object;
            if (null == l) {
                jsonSerializer.write(l);
            } else {
                jsonSerializer.write("@JSONField测试_" + l);
            }
        }
    }
}
