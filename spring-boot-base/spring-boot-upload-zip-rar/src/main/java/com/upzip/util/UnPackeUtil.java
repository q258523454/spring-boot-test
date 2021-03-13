package com.upzip.scanner.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import net.lingala.zip4j.core.ZipFile;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @Author: zhangj
 * @Date: 2019-10-08
 * @Version 1.0
 */
public class UnPackeUtil {
    private static final Logger logger = LoggerFactory.getLogger(UnPackeUtil.class);

    /**
     * zip文件解压
     *
     * @param destPath 解压文件路径
     * @param zipFile  压缩文件
     * @param password 解压密码(如果有)
     */
    public static void unPackZip(File zipFile, String password, String destPath) {
        long start = System.currentTimeMillis();
        try {
            ZipFile zip = new ZipFile(zipFile);
            zip.setFileNameCharset("GBK");
            // 如果解压需要密码
            if (zip.isEncrypted()) {
                zip.setPassword(password);
            }
            // zip4j默认用GBK编码去解压
            logger.info("begin unpack zip file....");
            zip.extractAll(destPath);
        } catch (Exception e) {
            logger.error("unPack zip file to " + destPath + " fail ....", e.getMessage(), e);
        }
        long end = System.currentTimeMillis();

        logger.info("finished uppack zip , time: {}", end - start);
    }

    /**
     * 注意: 不支持 rar5.0 以上的压缩文件
     * rar文件解压(不支持有密码的压缩包)
     *
     * @param rarFile  rar压缩包
     * @param destPath 解压保存路径
     */
    public static void unPackRar(File rarFile, String destPath) throws Exception {
        long start = System.currentTimeMillis();
        try (Archive archive = new Archive(rarFile, null)) {
            FileHeader fileHeader = archive.nextFileHeader();
            File file = null;
            while (null != fileHeader) {
                // 防止文件名中文乱码问题的处理
                String fileName = fileHeader.getFileNameW().isEmpty() ? fileHeader.getFileNameString() : fileHeader.getFileNameW();
                if (fileHeader.isDirectory()) {
                    //是文件夹
                    file = new File(destPath + File.separator + fileName);
                    file.mkdirs();
                } else {
                    //不是文件夹
                    file = new File(destPath + File.separator + fileName.trim());
                    if (!file.exists()) {
                        if (!file.getParentFile().exists()) {
                            // 相对路径可能多级，可能需要创建父目录.
                            file.getParentFile().mkdirs();
                        }
                        file.createNewFile();
                    }
                    FileOutputStream os = new FileOutputStream(file);
                    archive.extractFile(fileHeader, os);
                    os.close();
                }
                fileHeader = archive.nextFileHeader();
            }
        } catch (Exception e) {
            logger.error("unpack rar file fail....{}", e.getMessage(), e);
            throw e;
        }
        long end = System.currentTimeMillis();
        logger.info("finished uppack zip , time: {}", end - start);
    }
}
