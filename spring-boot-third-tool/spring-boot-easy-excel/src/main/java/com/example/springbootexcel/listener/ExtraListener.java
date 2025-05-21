package com.example.springbootexcel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson.JSON;
import com.example.springbootexcel.pojo.ExcelExtraData;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * 读取额外信息（批注、超链接、合并单元格信息读取）
 **/
@Slf4j
@Data
public class ExtraListener implements ReadListener<ExcelExtraData> {

    /**
     * 解析的数据T
     */
    List<ExcelExtraData> rowData = new ArrayList<>();


    /**
     * 合并单元格
     */
    private List<CellExtra> extraMergeInfoList = new ArrayList<>();


    @Override
    public void invoke(ExcelExtraData data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSON.toJSONString(data));
        rowData.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
    }

    @Override
    public void extra(CellExtra extra, AnalysisContext context) {
        switch (extra.getType()) {
            case COMMENT:
                log.info("【批注】,覆盖区间,在rowIndex:{},columnIndex;{},内容是:{}", extra.getRowIndex() + 1,
                        extra.getColumnIndex() + 1, extra.getText());
                break;
            case HYPERLINK:
                if ("Sheet1!A1".equals(extra.getText())) {
                    log.info("【超链接-sheet1-A1】,覆盖区间,在rowIndex:{},columnIndex;{},内容是:{}", extra.getRowIndex() + 1,
                            extra.getColumnIndex() + 1, extra.getText());
                } else if ("Sheet2!A1".equals(extra.getText())) {
                    log.info(
                            "【超链接-sheet2-A1】,覆盖区间,在firstRowIndex:{},firstColumnIndex;{},lastRowIndex:{},lastColumnIndex:{},"
                                    + "内容是:{}",
                            extra.getFirstRowIndex() + 1, extra.getFirstColumnIndex() + 1, extra.getLastRowIndex() + 1,
                            extra.getLastColumnIndex() + 1, extra.getText());
                } else {
                    Assert.fail("Unknown hyperlink!");
                }
                break;
            case MERGE:
                log.info(
                        "【合并单元】,覆盖区间,在firstRowIndex:{},firstColumnIndex;{},lastRowIndex:{},lastColumnIndex:{}",
                        extra.getFirstRowIndex() + 1, extra.getFirstColumnIndex() + 1, extra.getLastRowIndex() + 1,
                        extra.getLastColumnIndex() + 1);
                extraMergeInfoList.add(extra);
                break;
            default:
        }
    }

    public List<CellExtra> getExtraMergeInfoList() {
        return extraMergeInfoList;
    }
}
