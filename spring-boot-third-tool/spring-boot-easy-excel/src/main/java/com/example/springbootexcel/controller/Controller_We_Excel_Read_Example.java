package com.example.springbootexcel.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.fastjson.JSON;
import com.example.springbootexcel.listener.URLEventListener;
import com.example.springbootexcel.we.AddQuestionsBO;
import com.example.springbootexcel.we.ImportQuestionModel;
import com.example.springbootexcel.we.QuestionExcelReadListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
public class Controller_We_Excel_Read_Example {
    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    private Environment environment;

    /**
     * 读取文件/下载文件
     */
    @GetMapping(value = "/read/url")
    public String readUrl() {
        // 直接访问本地 resources/static 下的资源
        String localUrl = "http://localhost:8080/dept.xlsx";
        InputStream inputStream = null;
        URL url = null;
        try {
            url = new URL(localUrl);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(5000);
            inputStream = connection.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 一旦build(), IO流会关闭, 后面操作 ExcelReader 即可
        ExcelReader excelReader = EasyExcelFactory.read(inputStream, new URLEventListener()).build();
        List<ReadSheet> sheetList = excelReader.excelExecutor().sheetList();
        for (int i = 0; i < sheetList.size(); i++) {
            if (i == 0) {
                ReadSheet sheet = sheetList.get(i);
                log.info("读取 第" + i + "个sheet:" + sheet.getSheetName());
                excelReader.read(sheet);
            }
        }

        return "index";
    }


    @GetMapping(value = "/read/url2")
    public String readUrl2() {
        String port = environment.getRequiredProperty("server.port");
        String localUrl = "http://localhost:" + port + "/question_template.xlsx";
        InputStream inputStream = null;
        URL url = null;
        try {
            url = new URL(localUrl);
            URLConnection connection = url.openConnection();
            connection.setReadTimeout(30000);
            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Content-type", "application/json;charset=UTF-8");
            inputStream = connection.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        QuestionExcelReadListener listener = new QuestionExcelReadListener();
        // 指定sheet名, 节省IO资源.
        ExcelReaderBuilder read = EasyExcel.read(inputStream, ImportQuestionModel.class, listener);
        ExcelReaderSheetBuilder sheet = read.sheet("题目导入模板");
        sheet.headRowNumber(2).doRead();
        List<ImportQuestionModel> list = listener.getImportQuestionModelList();
        if (!list.isEmpty()) {
            return "success";
        }
        return "error";
    }


    /**
     * 使用 RestTemplate.getForEntity() 读取文件
     */
    @GetMapping(value = "/resttemplate/getforentity/read/url")
    public String restTemplateGetForEntity() {
        String port = environment.getRequiredProperty("server.port");
        String localUrl = "http://localhost:" + port + "/question_template.xlsx";
        byte[] body = restTemplate.getForEntity(localUrl, byte[].class).getBody();
        assert body != null;
        InputStream inputStream = new ByteArrayInputStream(body);
        QuestionExcelReadListener listener = new QuestionExcelReadListener();
        // 指定sheet名, 节省IO资源.
        ExcelReaderBuilder read = EasyExcel.read(inputStream, ImportQuestionModel.class, listener);
        ExcelReaderSheetBuilder sheet = read.sheet("题目导入模板");
        sheet.headRowNumber(2).doRead();
        List<ImportQuestionModel> list = listener.getImportQuestionModelList();
        if (!list.isEmpty()) {
            return "success";
        }
        return "error";
    }

    /**
     * 使用 RestTemplate.exchange() 读取文件2
     */
    @GetMapping(value = "/resttemplate/exchange/read/url")
    public String restTemplateExchange() {
        String port = environment.getRequiredProperty("server.port");
        String localUrl = "http://localhost:" + port + "/question_template.xlsx";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<byte[]> response = restTemplate.exchange(localUrl, HttpMethod.GET, entity, byte[].class);
        byte[] body = response.getBody();
        assert body != null;

        InputStream inputStream = new ByteArrayInputStream(body);
        QuestionExcelReadListener listener = new QuestionExcelReadListener();
        // 指定sheet名, 节省IO资源.
        ExcelReaderBuilder read = EasyExcel.read(inputStream, ImportQuestionModel.class, listener);
        ExcelReaderSheetBuilder sheet = read.sheet("题目导入模板");
        sheet.headRowNumber(2).doRead();
        List<ImportQuestionModel> list = listener.getImportQuestionModelList();
        if (!list.isEmpty()) {
            return "success";
        }
        return "error";
    }

    /**
     * 使用 RestTemplate 通过流读取
     */
    @GetMapping(value = "/resttemplate/execute/read/url")
    public String restTemplateExecute() {
        String port = environment.getRequiredProperty("server.port");
        String localUrl = "http://localhost:" + port + "/question_template.xlsx";
        QuestionExcelReadListener listener = new QuestionExcelReadListener();

        String res = restTemplate.execute(localUrl, HttpMethod.GET, null, clientHttpResponse -> {
            InputStream inputStream = clientHttpResponse.getBody();
            // 指定sheet名, 节省IO资源.
            ExcelReaderBuilder read = EasyExcel.read(inputStream, ImportQuestionModel.class, listener);
            ExcelReaderSheetBuilder sheet = read.sheet("题目导入模板");
            sheet.headRowNumber(2).doRead();
            List<ImportQuestionModel> list = listener.getImportQuestionModelList();
            if (!list.isEmpty()) {
                return "success";
            }
            return "error";
        });

        for (AddQuestionsBO addQuestionsBO : listener.getAddQuestionsList()) {
            log.info(JSON.toJSONString(addQuestionsBO));
        }
        return res;
    }
}
