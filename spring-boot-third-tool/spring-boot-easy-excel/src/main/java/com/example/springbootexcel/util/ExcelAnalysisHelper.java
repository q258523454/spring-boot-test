package com.example.springbootexcel.util;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.alibaba.excel.metadata.CellExtra;
import com.example.springbootexcel.listener.ExtraListener;
import com.example.springbootexcel.pojo.ExcelExtraData;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 1.合并单元格只有第一个（firstRowIndex，firstColumnIndex）有值，所以要取到这个值。
 * 2.通过获取到的合并单元格信息（firstRowIndex,lastRowIndex,firstColumnIndex,lastColumnIndex），遍历此区域的每一个单元格，并给每一个单元格都赋上该值
 * 3.此方法的重点在于利用反射找到实体对应的属性，对应关系是@ExcelProperty(index = 0)->columnIndex
 * index对应了columnIndex（也就是字段在excel所在的位置）；rowindex对应了解析出来的List<T> data的索引值
 */
public class ExcelAnalysisHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelAnalysisHelper.class);

    /**
     * 处理合并单元格
     *
     * @param data               解析数据
     * @param extraMergeInfoList 合并单元格信息
     * @param headRowNumber      起始行
     * @return 填充好的解析数据
     */
    public List<ExcelExtraData> explainMergeData(List<ExcelExtraData> data, List<CellExtra> extraMergeInfoList, Integer headRowNumber) {
        if (CollectionUtils.isEmpty(extraMergeInfoList)) {
            return data;
        }
        // 循环所有合并单元格信息
        extraMergeInfoList.forEach(cellExtra -> {
            int firstRowIndex = cellExtra.getFirstRowIndex() - headRowNumber;
            int lastRowIndex = cellExtra.getLastRowIndex() - headRowNumber;
            int firstColumnIndex = cellExtra.getFirstColumnIndex();
            int lastColumnIndex = cellExtra.getLastColumnIndex();
            // 获取初始值
            Object initValue = getInitValueFromList(firstRowIndex, firstColumnIndex, data);
            //  设置值
            for (int i = firstRowIndex; i <= lastRowIndex; i++) {
                for (int j = firstColumnIndex; j <= lastColumnIndex; j++) {
                    setInitValueToList(initValue, i, j, data);
                }
            }
        });
        return data;
    }

    /**
     * 设置合并单元格的值
     *
     * @param filedValue  值
     * @param rowIndex    行
     * @param columnIndex 列
     * @param data        解析数据
     */
    public void setInitValueToList(Object filedValue, Integer rowIndex, Integer columnIndex, List<ExcelExtraData> data) {
        if (rowIndex >= data.size()) {
            data.add(rowIndex, new ExcelExtraData());
        }
        ExcelExtraData object = data.get(rowIndex);
        for (Field field : object.getClass().getDeclaredFields()) {
            //提升反射性能，关闭安全检查
            field.setAccessible(true);
            ExcelProperty annotation = field.getAnnotation(ExcelProperty.class);
            if (annotation != null) {
                if (annotation.index() == columnIndex) {
                    try {
                        field.set(object, filedValue);
                        break;
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("解析数据时发生异常!");
                    }
                }
            }
        }
    }

    /**
     * 获取合并单元格的初始值
     * rowIndex对应list的索引
     * columnIndex对应实体内的字段
     *
     * @param firstRowIndex    起始行
     * @param firstColumnIndex 起始列
     * @param data             列数据
     * @return 初始值
     */
    private Object getInitValueFromList(Integer firstRowIndex, Integer firstColumnIndex, List<ExcelExtraData> data) {
        Object filedValue = null;
        ExcelExtraData object = data.get(firstRowIndex);
        for (Field field : object.getClass().getDeclaredFields()) {
            //提升反射性能，关闭安全检查
            field.setAccessible(true);
            ExcelProperty annotation = field.getAnnotation(ExcelProperty.class);
            if (annotation != null) {
                if (annotation.index() == firstColumnIndex) {
                    try {
                        filedValue = field.get(object);
                        break;
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("解析数据时发生异常!");
                    }
                }
            }
        }
        return filedValue;
    }
}