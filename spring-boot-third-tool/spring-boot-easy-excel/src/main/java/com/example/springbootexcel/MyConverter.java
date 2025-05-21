package com.example.springbootexcel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;

/**
 * @date 2022-07-27 19:44
 */
public class MyConverter implements Converter<String> {
    @Override
    public String convertToJavaData(ReadConverterContext<?> context) throws Exception {
        return "(自定义Converter)" + context.getReadCellData().getStringValue();
    }
}
