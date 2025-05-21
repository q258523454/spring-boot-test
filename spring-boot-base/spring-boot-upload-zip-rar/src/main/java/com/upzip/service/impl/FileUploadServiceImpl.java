package com.upzip.service.impl;

import com.upzip.entity.FileTypeEnum;
import com.upzip.scanner.entity.PackParam;
import com.upzip.scanner.util.UnPackeUtil;
import com.upzip.service.FileUploadService;
import com.upzip.util.AjaxList;
import com.upzip.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    @Override
    public AjaxList<String> handlerUpload(MultipartFile zipFile, PackParam packParam) {

        if (null == zipFile) {
            return AjaxList.createFail("请上传压缩文件!");
        }
        boolean isZipPack = true;
        String fileContentType = zipFile.getContentType();
        //将压缩包保存在指定路径

        String fileName = "";
        String packFilePath = "";
        if (FileTypeEnum.FILE_TYPE_ZIP.type.equals(fileContentType)) {
            //zip解压缩处理
            fileName = zipFile.getOriginalFilename().substring(0, zipFile.getOriginalFilename().lastIndexOf(".zip"));
            String newFileName = fileName + DateUtil.INSTANCE.getYmdHmsSSS();
            packFilePath = packParam.getDestPath() + File.separator + newFileName;
            packFilePath += FileTypeEnum.FILE_TYPE_ZIP.fileStufix;
        } else if (FileTypeEnum.FILE_TYPE_RAR.type.equals(fileContentType)) {
            //rar解压缩处理
            fileName = zipFile.getOriginalFilename().substring(0, zipFile.getOriginalFilename().lastIndexOf(".rar"));
            String newFileName = fileName + DateUtil.INSTANCE.getYmdHmsSSS();
            packFilePath = packParam.getDestPath() + File.separator + newFileName;
            packFilePath += FileTypeEnum.FILE_TYPE_RAR.fileStufix;
            isZipPack = false;
        } else {
            return AjaxList.createFail("上传的压缩包格式不正确,仅支持rar和zip压缩文件!");
        }
        File file = new File(packFilePath);
        try {
            zipFile.transferTo(file);
        } catch (IOException e) {
            logger.error("zip file save to " + packParam.getDestPath() + " error", e.getMessage(), e);
            return AjaxList.createFail("保存压缩文件到:" + packParam.getDestPath() + " 失败!");
        }
        if (isZipPack) {
            //zip压缩包
            UnPackeUtil.unPackZip(file, packParam.getPassword(), packParam.getDestPath());
        } else {
            //rar压缩包
            try {
                UnPackeUtil.unPackRar(file, packParam.getDestPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return AjaxList.createSuccess("解压成功");
    }
}