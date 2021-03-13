package com.ftp_util;

import org.apache.commons.net.ftp.FTPClient;

import java.util.List;
import java.util.Map;

public interface FTPUtil {

    /***
     * 获取path下所有的文件名
     * @param ftpClient
     * @param folderPath
     * @return
     */
    public List<String> getFolderFileLists(FTPClient ftpClient, String folderPath);

    /**
     * 获取path下所有的文件名, 按照创建时间降序
     * @param ftpClient
     * @param folderPath
     * @return Map<fileName, createTimeStamp>
     */
    public Map<String, String> getFolderFileListsByCreateTimeDesc(FTPClient ftpClient, String folderPath);

    /**
     * 读取txt文件内容
     */
    public boolean readTxtFile(FTPClient ftpClient, String filePath, String fileName);

    /**
     * 遍历解析文件夹下所有文件
     *
     * @param folderPath 需要解析的的文件夹
     * @param ftpClient  FTPClient对象
     * @return
     */
    public boolean readTxtFileByFolder(FTPClient ftpClient, String folderPath);

    /**
     * 下载FTP下指定文件
     *
     * @param ftpClient FTPClient对象
     * @param filePath  FTP文件路径
     * @param fileName  文件名
     * @param downPath  下载保存的目录
     * @return
     */
    public boolean downLoadFTP(FTPClient ftpClient, String filePath, String fileName, String downPath);

    /**
     * FTP文件上传工具类
     *
     * @param ftpClient
     * @param localFilePath
     * @param ftpPath
     * @return
     */
    public boolean uploadFile(FTPClient ftpClient, String localFilePath, String ftpPath);

    /**
     * FPT上文件的复制
     *
     * @param ftpClient FTPClient对象
     * @param olePath   原文件地址
     * @param newPath   新保存地址
     * @param fileName  文件名
     * @return
     */
    public boolean copyFile(FTPClient ftpClient, String olePath, String newPath, String fileName);

    /**
     * 实现文件的移动，这里做的是一个文件夹下的所有内容移动到新的文件，
     * 如果要做指定文件移动，加个判断判断文件名
     * 如果不需要移动，只是需要文件重命名，可以使用ftp.rename(oleName,newName)
     *
     * @param ftpClient
     * @param oldPath
     * @param newPath
     * @return
     */
    public boolean moveFile(FTPClient ftpClient, String oldPath, String newPath);

    /**
     * 删除FTP上指定文件夹下文件及其子文件方法，添加了对中文目录的支持
     *
     * @param ftpClient FTPClient对象
     * @param FtpFolder 需要删除的文件夹
     * @return
     */
    public boolean deleteByFolder(FTPClient ftpClient, String FtpFolder);
}
