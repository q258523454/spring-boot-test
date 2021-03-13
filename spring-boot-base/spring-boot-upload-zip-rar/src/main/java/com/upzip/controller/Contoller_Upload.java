package com.upzip.controller;


import com.upzip.service.FileUploadService;
import com.upzip.util.AjaxList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Date: 2019-05-10
 * @Version 1.0
 */

@RestController
public class Contoller_Upload {

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/upload/zip")
    public String uploadZip(MultipartFile zipFile, com.upzip.scanner.entity.PackParam packParam) {
        AjaxList<String> ajaxList = fileUploadService.handlerUpload(zipFile, packParam);
        return ajaxList.getData();
    }
}
