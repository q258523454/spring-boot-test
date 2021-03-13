package com.upzip.service;

import com.upzip.scanner.entity.PackParam;
import com.upzip.util.AjaxList;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: zhangj
 * @Date: 2019-10-08
 * @Version 1.0
 */
public interface FileUploadService {
    AjaxList<String> handlerUpload(MultipartFile zipFile, PackParam packParam);
}
