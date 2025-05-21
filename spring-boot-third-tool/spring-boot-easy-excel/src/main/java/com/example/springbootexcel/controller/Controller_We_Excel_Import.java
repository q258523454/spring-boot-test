package com.example.springbootexcel.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.fastjson.JSON;
import com.example.springbootexcel.we.ImportQuestionModel;
import com.example.springbootexcel.we.QuestionExcelReadListener;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.util.List;

@Slf4j
@RestController
public class Controller_We_Excel_Import {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Environment environment;

    /**
     * 使用 RestTemplate 通过流读取
     */
    @GetMapping(value = "/import/question")
    public String importQuestion() {
        String port = environment.getRequiredProperty("server.port");
        String localUrl = "http://localhost:" + port + "/question_template.xlsx";
        QuestionExcelReadListener listener = new QuestionExcelReadListener();
        String sheetName = loadQuestionFileBySheetName(localUrl, listener, ImportQuestionModel.class, 0, 2);
        log.info(JSON.toJSONString(listener.getImportQuestionModelList()));
        return "" + sheetName;
    }


    public <T> String loadQuestionFileBySheetName(String httpURL, ReadListener<T> listener, Class<T> clazz,
            int sheetNo, int headRowNumber) {
        String result = "";
        try {
            result = restTemplate.execute(httpURL, HttpMethod.GET, null, clientHttpResponse -> {
                InputStream inputStream = clientHttpResponse.getBody();
                // 指定sheet名, 节省IO资源.一旦build,IO流就会关闭,后序操作 excelReader
                ExcelReader excelReader = EasyExcel.read(inputStream, clazz, listener)
                        // 读取合并单元信息
                        .extraRead(CellExtraTypeEnum.MERGE).build();
                List<ReadSheet> sheetList = excelReader.excelExecutor().sheetList();
                ReadSheet sheet = sheetList.get(sheetNo);
                String sheetName = sheet.getSheetName();
                log.info("sheetNo:{},sheet name:{}", sheetNo, sheetName);
                sheet.setHeadRowNumber(headRowNumber);
                excelReader.read(sheet);
                return sheetName;
            });
        } catch (Exception e) {
            log.error("download file error:", e);
        }
        return result;
    }


    public <T> Integer loadQuestionFileBySheetNo(String httpURL, ReadListener<T> listener, Class<T> clazz,
            int sheetNo, int headRowNumber) {
        Integer result;
        try {
            result = restTemplate.execute(httpURL, HttpMethod.GET, null, clientHttpResponse -> {
                InputStream inputStream = clientHttpResponse.getBody();
                ExcelReaderBuilder read = EasyExcel.read(inputStream, clazz, listener);
                // 指定sheetNo名, 节省IO资源.
                ExcelReaderSheetBuilder sheet = read.sheet(sheetNo);
                // 跳过head行
                sheet.headRowNumber(headRowNumber).doRead();
                inputStream.close();
                return 1;
            });
        } catch (Exception e) {
            log.error("download file error:", e);
            result = 0;
        }
        return result;
    }
}
