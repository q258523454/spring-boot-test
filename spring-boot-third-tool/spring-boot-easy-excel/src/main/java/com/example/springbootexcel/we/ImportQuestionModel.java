
package com.example.springbootexcel.we;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

/**
 * ImportQuestionModel
 */
@Data
@FieldNameConstants
public class ImportQuestionModel {
    @ExcelProperty(value = "题型", index = 0)
    private String type;

    @ExcelProperty(value = "题目", index = 1)
    private String title;

    @ExcelProperty(value = "选项1", index = 2)
    private String option1;

    @ExcelProperty(value = "选项2", index = 3)
    private String option2;

    @ExcelProperty(value = "选项3", index = 4)
    private String option3;

    @ExcelProperty(value = "选项4", index = 5)
    private String option4;

    @ExcelProperty(value = "选项5", index = 6)
    private String option5;

    @ExcelProperty(value = "选项6", index = 7)
    private String option6;

    @ExcelProperty(value = "选项7", index = 8)
    private String option7;

    @ExcelProperty(value = "选项8", index = 9)
    private String option8;

    @ExcelProperty(value = "选项9", index = 10)
    private String option9;

    @ExcelProperty(value = "选项10", index = 11)
    private String option10;

    @ExcelProperty(value = "选项11", index = 12)
    private String option11;

    @ExcelProperty(value = "选项12", index = 13)
    private String option12;

    @ExcelProperty(value = "选项13", index = 14)
    private String option13;

    @ExcelProperty(value = "选项14", index = 15)
    private String option14;

    @ExcelProperty(value = "选项15", index = 16)
    private String option15;

    @ExcelProperty(value = "选项16", index = 17)
    private String option16;

    @ExcelProperty(value = "选项17", index = 18)
    private String option17;

    @ExcelProperty(value = "选项18", index = 19)
    private String option18;

    @ExcelProperty(value = "选项19", index = 20)
    private String option19;

    @ExcelProperty(value = "选项20", index = 21)
    private String option20;
}
