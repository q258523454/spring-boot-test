package com.example.springbootexcel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.holder.ReadRowHolder;
import com.alibaba.excel.read.metadata.holder.ReadSheetHolder;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 复杂Excel结构用 Map 解析
 */
@Slf4j
@Getter
public class MapAnalysisEventListener extends AnalysisEventListener<Map<Integer, Object>> {

    // excel 第二行为标题
    private Map<Integer, Object> headMap = new HashMap<>();
    private List<Map<Integer, Object>> contentList = new ArrayList<>();
    private String dept;

    /**
     * 用于解析复杂excel
     * @param contentMap    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context analysis context
     */
    @Override
    public void invoke(Map<Integer, Object> contentMap, AnalysisContext context) {
        ReadRowHolder readRowHolder = context.readRowHolder();
        Integer rowIndex = readRowHolder.getRowIndex();

        // rowIndex=1 表示第二行
        if (rowIndex == 1) {
            this.headMap = contentMap;
        } else {
            for (Map.Entry<Integer, Object> entry : contentMap.entrySet()) {
                Integer key = entry.getKey();
                Object value = entry.getValue();

                if (key == 0) {
                    if (value == null) {
                        contentMap.replace(key, dept);
                    } else {
                        dept = String.valueOf(value);
                    }
                }
            }
        }
        log.info("<{}> content: {} ", rowIndex, JSONObject.toJSONString(contentMap));
        this.contentList.add(contentMap);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        ReadSheetHolder readSheetHolder = context.readSheetHolder();
        String sheetName = readSheetHolder.getSheetName();
        log.info("<{}> read successfully! total: {}", sheetName, this.contentList.size());

    }

    public Map<Integer, Object> getHeadMap() {
        return headMap;
    }

    public List<Map<Integer, Object>> getContentList() {
        return contentList;
    }
}