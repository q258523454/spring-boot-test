package com.example.springbootexcel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.holder.ReadRowHolder;
import com.alibaba.excel.read.metadata.holder.ReadSheetHolder;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 从 URL 资源解析 文件
 */
@Slf4j
@Getter
public class URLEventListener extends AnalysisEventListener<Map<Integer, Object>> {

    @Override
    public void invoke(Map<Integer, Object> contentMap, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSON.toJSONString(contentMap));
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }

}