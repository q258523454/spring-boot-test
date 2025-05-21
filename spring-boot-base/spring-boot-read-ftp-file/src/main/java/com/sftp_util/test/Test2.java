package com.sftp_util.test;

import com.jcraft.jsch.ChannelSftp;
import com.sftp_util.SFTPClient;
import com.sftp_util.SFTPConfig;
import com.sftp_util.SFTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Date: 2019-06-19
 * @Version 1.0
 */
public class Test2 {
    private final static Logger log = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) {

        String ip = "xxx";
        String username = "qadmsom";
        String pwd = "xxx@xx#";
        Integer port = xx;
        Integer timeout = xx;

        SFTPConfig sftpConfig = new SFTPConfig(ip, port, username, pwd, timeout);


        ChannelSftp sftp = null;
        String sftpPath = "/xxx";
        String directoryPrefix = "20";
        String filePrefix = "gbk";

        try {
            sftp = SFTPClient.connect(sftpConfig);
            String datFilePath = SFTPUtil.getFolderRecentDirectoryAndRecentFilePathByPrefix(sftp, sftpPath, directoryPrefix, filePrefix);
            System.out.println(datFilePath);
            SFTPUtil.readTxtDataGBK(sftp, datFilePath);
            SFTPClient.disConnect(sftp);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            // 必须安全关闭
            if (null != sftp && sftp.isConnected()) {
                SFTPClient.disConnect(sftp);
            }
        }
    }
}
