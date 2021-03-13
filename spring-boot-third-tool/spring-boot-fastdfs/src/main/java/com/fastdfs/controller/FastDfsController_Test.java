package com.fastdfs.controller;

import com.alibaba.fastjson.JSONObject;
import com.fastdfs.global.FastDFSConfig;
import com.fastdfs.service.impl.FastDFSClientImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;

/**
 * Created By
 *
 * @date :   2018-09-20
 */

@RestController
public class FastDfsController_Test {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FastDFSConfig fastDFSConfig;

    private UrlPathHelper urlPathHelper = new UrlPathHelper();


    // 测试字节上传文件
    @GetMapping(value = "/testUploadFileByte", produces = "application/json; charset=UTF-8")
    public String testUploadFileByte(HttpServletRequest request) {
        try {
            String imagePath = "/Users/mac/Pictures/刘德华头像2.JPEG";
            File file = new File(imagePath);
            String fileExt = imagePath.substring(imagePath.lastIndexOf(".") + 1);
            byte[] fileByte = Files.readAllBytes(file.toPath());
            FastDFSClientImpl fastDFSClientImpl = new FastDFSClientImpl(fastDFSConfig);
            String storagePath = fastDFSClientImpl.uploadFile(fileByte, fileExt);
            return JSONObject.toJSONString("Success, Upload path is:" + storagePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSONObject.toJSONString("Failed to upload file.");
    }


    // 上传(增)
    @GetMapping(value = "/uploadFileByPath", produces = "application/json; charset=UTF-8")
    public String uploadFileByPath(HttpServletRequest request) throws Exception {
        FastDFSClientImpl fastDFSClientImpl = new FastDFSClientImpl(fastDFSConfig);
        String filePath = fastDFSClientImpl.uploadFile("/Users/mac/Pictures/刘德华头像1.JPEG");
        System.out.println("Success, Upload path is ：" + filePath);
        return JSONObject.toJSONString("Success, Upload path is:" + filePath);
    }


    // 上传(增)
    @GetMapping(value = "/uploadFile", produces = "application/json; charset=UTF-8")
    public String uploadFile(HttpServletRequest request, MultipartFile multipartFile) {
        try {
            FastDFSClientImpl fastDFSClientImpl = new FastDFSClientImpl(fastDFSConfig);
            String filePathNameExt = multipartFile.getOriginalFilename();
            String fileExt = filePathNameExt.substring(filePathNameExt.lastIndexOf(".") + 1);   // 获取文件后缀
            byte[] fileByte = multipartFile.getBytes(); // 文件字节信息
            String storagePath = fastDFSClientImpl.uploadFile(fileByte, fileExt);
            return JSONObject.toJSONString("Success, Upload path is:" + storagePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSONObject.toJSONString("Failed to upload file.");
    }


    // 查看(查),返回文件在服务器的信息
    @GetMapping(value = "/getFileInfo", produces = "application/json; charset=UTF-8")
    public String getFileInfo(HttpServletRequest request, String storageFilePath) throws Exception {
        FastDFSClientImpl fileClient = new FastDFSClientImpl(fastDFSConfig);
        String fileInfo = fileClient.getFileInfo(storageFilePath);
        return JSONObject.toJSONString(fileInfo);
    }

    // 下载
    @GetMapping(value = "/downloadFile")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response, String storageFilePath) throws Exception {
        FastDFSClientImpl fastDFSClientImpl = new FastDFSClientImpl(fastDFSConfig);
        byte[] fileByte = fastDFSClientImpl.downloadFile(storageFilePath);
        if (null == fileByte) {
            log.error("Failed to obtain byte of file ");
        } else {
            log.info("Success obtain byte of file ");
            String fileName = "测试下载.jpeg";
            response.setContentType("multipart/form-data"); // multipart/form-data will auto determined
            // fileName=测试.jpg;filename*=utf-8''%E6%B5%8B%E8%AF%95.jpg, 这种格式可以解决中文乱码
            response.setHeader("Content-Disposition", "attachment; fileName=" + fileName + ";filename*=utf-8''" + URLEncoder.encode(fileName, "UTF-8"));
            OutputStream out = response.getOutputStream();
            out.write(fileByte);
            out.flush();
            out.close();
        }
    }


    // 删除(删)
    @GetMapping(value = "/deleteFile", produces = "application/json; charset=UTF-8")
    public String deleteFile(HttpServletRequest request, String storageFilePath) throws Exception {
        FastDFSClientImpl fastDFSClientImpl = new FastDFSClientImpl(fastDFSConfig);
        Integer result = fastDFSClientImpl.deleteFile(storageFilePath);
        String response = result == -1 ? ("delete failed:[" + result + "]") : ("delete success:[" + result + "]");
        return JSONObject.toJSONString(response);
    }

}
