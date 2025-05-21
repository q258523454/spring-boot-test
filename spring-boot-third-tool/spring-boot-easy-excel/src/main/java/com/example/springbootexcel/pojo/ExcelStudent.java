package com.example.springbootexcel.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.example.springbootexcel.MyConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelStudent {
    // ExcelProperty中的参数要对应Excel中的标题
    @ExcelProperty("ID")
    private int id;

    // 自定义 converter
    @ExcelProperty(value = "NAME", converter = MyConverter.class)
    private String name;

    @ExcelProperty("AGE")
    private int age;
}
