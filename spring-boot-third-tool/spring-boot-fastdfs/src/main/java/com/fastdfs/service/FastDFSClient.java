package com.fastdfs.service;

/**
 * Created By
 *
 * @date :   2018-09-20
 */
public interface FastDFSClient {


    /**
     * 获取文件信息
     *
     * @param storageFilePath group1/M00/00/00/test.JPEG
     * @return null:失败
     */
    String getFileInfo(String storageFilePath) throws Exception;


    /**
     * 上传文件
     *
     * @param filePathAndName 文件的磁盘路径名称 如：/Users/mac/Pictures/a.JPEG
     * @return null:失败, group path:成功
     */
    String uploadFile(String filePathAndName) throws Exception;


    /**
     * 上传文件2
     *
     * @param fileBytes   图片文件字节流
     * @param fileExtName 文件格式后缀
     * @return null:失败, group path:成功
     */
    String uploadFile(byte[] fileBytes, String fileExtName) throws Exception;


    /**
     * 下载文件(字节)
     *
     * @param storageFilePath
     * @return null:失败
     */
    byte[] downloadFile(String storageFilePath) throws Exception;


    /**
     * 删除文件
     *
     * @param storageFilePath group1/M00/00/00/test.JPEG
     * @return -1:失败,  0:成功
     */
    Integer deleteFile(String storageFilePath) throws Exception;

}
