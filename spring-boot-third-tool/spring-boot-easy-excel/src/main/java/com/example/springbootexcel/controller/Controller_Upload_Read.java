package com.example.springbootexcel.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.fastjson.JSON;
import com.example.springbootexcel.listener.CellDataListener;
import com.example.springbootexcel.listener.ExtraListener;
import com.example.springbootexcel.listener.MapAnalysisEventListener;
import com.example.springbootexcel.listener.StudentReadListener;
import com.example.springbootexcel.pojo.ExcelCellData;
import com.example.springbootexcel.pojo.ExcelStudent;
import com.example.springbootexcel.pojo.ExcelExtraData;
import com.example.springbootexcel.util.ExcelAnalysisHelper;

import lombok.extern.slf4j.Slf4j;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class Controller_Upload_Read {


    /**
     * 常规读取
     * 按自定义实体类+converter
     */
    @PostMapping(value = "/file/student")
    public String studentFile(
            @Valid
            @NotNull
            @RequestPart(value = "file", required = true) MultipartFile multipartFile) throws IOException {
        log.info("file");
        InputStream in = null;
        // 从multipartFile获取InputStream流
        in = multipartFile.getInputStream();

        // 一旦build(), IO流会关闭, 后面操作 ExcelReader 即可
        ExcelReader excelReader = EasyExcel.read(in, ExcelStudent.class, new StudentReadListener()).build();
        // 获取全部sheet,只读取第2个.
        List<ReadSheet> sheetList = excelReader.excelExecutor().sheetList();
        for (ReadSheet readSheet : sheetList) {
            // 只读取第3个sheet(下标从0开始)
            if (readSheet.getSheetNo() == 2) {
                excelReader.read(readSheet);
            }
        }

        // 再次获取流, 只读取 sheet2, 节省IO资源.
        in = multipartFile.getInputStream();
        ExcelReaderBuilder read = EasyExcel.read(in, ExcelStudent.class, new StudentReadListener());
        // 执行 read.sheet 后会关闭流, 前3行是行头.行头的执行方法是: invokeHead
        ExcelReaderSheetBuilder sheet = read.sheet("Sheet1").headRowNumber(3);
        sheet.doRead();

        return "file";
    }

    /**
     * 复杂读取
     * 读取复杂表单为 Map 结构
     * {@link AnalysisEventListener}
     */
    @PostMapping(value = "/file/dept")
    public String dept(
            @Valid @NotNull @RequestPart(value = "file", required = true) MultipartFile multipartFile) {
        MapAnalysisEventListener listener = new MapAnalysisEventListener();
        ExcelReader excelReader = null;
        List<ReadSheet> readSheetList = null;
        try {
            // 创建 readerBuilder (自定义listener), 会在 read*()方法的时候执行监听器.
            ExcelReaderBuilder readerBuilder = EasyExcel.read(multipartFile.getInputStream(), listener);
            excelReader = readerBuilder.build();
            // 获取 sheet
            readSheetList = excelReader.excelExecutor().sheetList();
        } catch (IOException e) {
            log.warn("excel文件错误!");
        }
        if (StringUtils.isEmpty(readSheetList)) {
            log.warn("请选择一个有效的excel文件!");
            throw new RuntimeException("excel file is empty!");
        }
        // 依次读取每个 sheet
        for (ReadSheet readSheet : readSheetList) {
            excelReader.read(readSheet);
            Map<Integer, Object> headMap = listener.getHeadMap();
            List<Map<Integer, Object>> contentList = listener.getContentList();
            log.info("[{}] to be handling data!", readSheet.getSheetName());
        }
        return "ok";
    }

    /**
     * 额外信息（批注、超链接、合并单元格信息读取）
     * 由于是流式读取，没法在读取到单元格数据的时候直接读取到额外信息，所以只能最后通知哪些单元格有哪些额外信息
     * 默认异步读取 excel, 即一次只读一条数据, 而不是一次把整个Excel文件加载到内存.
     */
    @GetMapping(value = "/file/extra")
    public void extraExcel() throws URISyntaxException {
        URL resource = this.getClass().getClassLoader().getResource("static/extra.xlsx");
        assert resource != null;
        File file = Paths.get(resource.toURI()).toFile();
        String fileName = file.getAbsolutePath();
        ExtraListener listener = new ExtraListener();
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet
        EasyExcel.read(fileName, ExcelExtraData.class, listener)
                // 需要读取批注 默认不读取
                .extraRead(CellExtraTypeEnum.COMMENT)
                // 需要读取超链接 默认不读取
                .extraRead(CellExtraTypeEnum.HYPERLINK)
                // 需要读取合并单元格信息 默认不读取
                .extraRead(CellExtraTypeEnum.MERGE).sheet().doRead();

        ExcelAnalysisHelper helper = new ExcelAnalysisHelper();
        helper.explainMergeData(listener.getRowData(), listener.getExtraMergeInfoList(), 1);
        System.out.println(JSON.toJSONString(listener.getRowData()));
    }

    /**
     * 公式日期等
     */
    @PostMapping(value = "/file/cell")
    public void cellExcel() throws URISyntaxException {
        URL resource = this.getClass().getClassLoader().getResource("static/cell.xlsx");
        assert resource != null;
        File file = Paths.get(resource.toURI()).toFile();
        String fileName = file.getAbsolutePath();
        EasyExcel.read(fileName, ExcelCellData.class, new CellDataListener()).sheet().doRead();
    }
}
