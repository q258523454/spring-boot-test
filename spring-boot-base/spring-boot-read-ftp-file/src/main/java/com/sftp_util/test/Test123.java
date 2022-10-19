package com.sftp_util.test;

import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import com.sftp_util.SFTPClient;
import com.sftp_util.SFTPConfig;
import com.sftp_util.SFTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * @Date: 2019-06-19
 * @Version 1.0
 */
public class Test {

    private final static Logger log = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) {

        String ip = "1.1.1.26";
        String username = "2";
        String pwd = "3#";
        Integer port = 22;
        Integer timeout = 5000;
        String root = "/aaa";

        ChannelSftp sftp = null;
        try {
            SFTPConfig sftpConfig = new SFTPConfig(ip, port, username, pwd, timeout);

            sftp = SFTPClient.connect(sftpConfig);
            SFTPUtil.readTxtDataUTF8(sftp, "/ndlj02_local/a.txt");
            SFTPClient.disConnect(sftp);

            sftp = SFTPClient.connect(sftpConfig);
            List<String> list = SFTPUtil.getFileListByPath(sftp, root);
            SFTPClient.disConnect(sftp);

            log.info(JSONObject.toJSONString(list));
        } catch (SftpException | IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            // 必须安全关闭
            if (null != sftp && sftp.isConnected()) {
                SFTPClient.disConnect(sftp);
            }
        }


    }
}
