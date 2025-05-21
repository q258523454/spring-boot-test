package com.fastdfs.service.impl;

import com.fastdfs.service.FastDFSClient;
import com.fastdfs.global.FastDFSConfig;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Properties;


public class FastDFSClientImpl implements FastDFSClient {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private TrackerServer trackerServer = null;
    private TrackerClient trackerClient = null;

    private StorageServer storageServer = null;
    private StorageClient1 storageClient1 = null;

    // 由于可能多个文件服务器配置, 这里用构造函数来创建服务
    public FastDFSClientImpl(FastDFSConfig fastDFSConfig) throws IOException, MyException {
        Properties props = new Properties();
        props.put(ClientGlobal.PROP_KEY_TRACKER_SERVERS, fastDFSConfig.getTracker_servers());                           // tracker服务器IP和端口
        props.put(ClientGlobal.PROP_KEY_CONNECT_TIMEOUT_IN_SECONDS, fastDFSConfig.getConnect_timeout_in_seconds());     // 连接tracker服务器超时时长
        props.put(ClientGlobal.PROP_KEY_NETWORK_TIMEOUT_IN_SECONDS, fastDFSConfig.getNetwork_timeout_in_seconds());     // socket连接超时时长
        props.put(ClientGlobal.PROP_KEY_CHARSET, fastDFSConfig.getCharset());                                           // 文件内容编码
        ClientGlobal.initByProperties(props);

        trackerClient = new TrackerClient();
        trackerServer = trackerClient.getConnection();
        storageClient1 = new StorageClient1(trackerServer, null);

        log.info("FastDFS Settings information :\n" + ClientGlobal.configInfo());
    }

    /**
     * 上传文件
     *
     * @param filePathAndName 文件的磁盘路径名称 如：/Users/mac/Pictures/a.JPEG
     * @return null:失败, group path:成功
     */
    @Override
    public String uploadFile(String filePathAndName) throws Exception {
        String result = storageClient1.upload_file1(filePathAndName, null, null);
        if (null == result) {
            log.error("fdfs upload file error:" + storageClient1.getErrorCode());
        }
        return result;
    }

    /**
     * 上传文件2
     *
     * @param fileBytes   图片文件字节流
     * @param fileExtName 文件格式后缀
     * @return null:失败, group path:成功
     */
    @Override
    public String uploadFile(byte[] fileBytes, String fileExtName) throws Exception {
        String result = storageClient1.upload_file1(fileBytes, fileExtName, null);
        if (null == result) {
            log.error("fdfs upload file error:" + storageClient1.getErrorCode());
        }
        return result;
    }

    /**
     * 获取文件信息
     *
     * @param storageFilePath group1/M00/00/00/test.JPEG
     * @return null:失败
     */
    @Override
    public String getFileInfo(String storageFilePath) throws Exception {
        FileInfo fileInfo = storageClient1.get_file_info1(storageFilePath);
        if (null == fileInfo) {
            log.error("fdfs upload file error:" + storageClient1.getErrorCode());
            return null;
        }
        return fileInfo.toString();
    }


    /**
     * 下载文件(字节)
     *
     * @param storageFilePath
     * @return null:失败
     */
    @Override
    public byte[] downloadFile(String storageFilePath) throws Exception {
        byte[] fileByte = storageClient1.download_file1(storageFilePath);
        if (null == fileByte) {
            log.error("fdfs download file error:" + storageClient1.getErrorCode());
        }
        return fileByte;
    }

    /**
     * 删除文件
     *
     * @param storageFilePath group1/M00/00/00/CgEBDlujlNSABYx0AAF-SBVqisw76.JPEG"
     * @return -1:失败,  0:成功
     */
    @Override
    public Integer deleteFile(String storageFilePath) throws Exception {
        Integer result = -1;
        result = storageClient1.delete_file1(storageFilePath);
        if (-1 == result) {
            log.error("fdfs delete file error:" + storageClient1.getErrorCode());
        }
        return result;
    }

}
