package com.ftp_util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.net.ftp.FTPClient;

import java.util.List;

/**
 * @Date: 2019-05-27
 * @Version 1.0
 */
public class Test {
    public static void main(String args[]) {

        String ftpPath = "/home/prod/aaa/ftp";
        String txtFileName = "test1.DAT";
        String newFtpPath = "/home/prod/aaa/ftp2";
        String downLoadPath = "G:\\我的下载";

        FTPClientUtil ftpClientUtil = new FTPClientUtil();
        FTPUtil test = new FTPUtilImpl();
        FTPClient ftpClient = ftpClientUtil.getFTPClient("55.14.4.26", 21, "qadmsom", "i@Yk8gK#");

//        // 测试: 下载文件
//        test.downLoadFTP(ftpClient, ftpPath, txtFileName, downLoadPath);
//
//        // 测试: ftp服务器上复制文件
//        test.copyFile(ftpClient, "/file", "/txt/temp", "你好.txt");
//
//        // 测试: 移动文件
//        test.moveFile(ftpClient, ftpPath, newFtpPath);
//
//        // 测试: 上传文件
//        test.uploadFile(ftpClient, "G:\\我的下载\\abcd.txt", ftpPath);
//
//        // 测试: 删除文件夹
//        test.deleteByFolder(ftpClient, newFtpPath);
//
        // 测试: 解析文件夹下所有txt文件
//        test.readTxtFileByFolder(ftpClient, ftpPath);

        List<String> fileLists= test.getFolderFileLists(ftpClient, ftpPath);
        System.out.println(JSON.toJSONString(fileLists));

        test.readTxtFile(ftpClient, ftpPath, txtFileName);
//        ftpClientUtil.closeFTP(ftpClient);
//        test.getFolderFileListsByCreateTimeDesc(ftpClient, ftpPath);
        System.exit(0);
    }

}
