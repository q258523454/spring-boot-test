package com.example.springbootexcel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson.JSON;
import com.example.springbootexcel.pojo.ExcelCellData;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 读取头
 */
@Slf4j
public class CellDataListener implements ReadListener<ExcelCellData> {

    @Override
    public void invoke(ExcelCellData data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSON.toJSONString(data));
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
