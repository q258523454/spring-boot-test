package com.example.springbootexcel.pojo;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class ExcelExtraData {

    /**
     * index 是指定第几列(从0开始),值是固定的,不管是否为空
     * order 是顺序往后递推(跳过空列)
     * 如果想不空列则需要加入order字段，而不是index,order会忽略空列，然后继续往后，而index，不会忽略空列，在第几列就是第几列。
     */

    @ExcelProperty(value = "第一列", index = 0)
    private String row1;

    @ExcelProperty(value = "第二列", index = 1)
    private String row2;
}

