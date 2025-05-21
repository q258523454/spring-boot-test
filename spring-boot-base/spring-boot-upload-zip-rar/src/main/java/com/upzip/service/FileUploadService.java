package com.upzip.service;

import com.upzip.scanner.entity.PackParam;
import com.upzip.util.AjaxList;
import org.springframework.web.multipart.MultipartFile;


public interface FileUploadService {
    AjaxList<String> handlerUpload(MultipartFile zipFile, PackParam packParam);
}
