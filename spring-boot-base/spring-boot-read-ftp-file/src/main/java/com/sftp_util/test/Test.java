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

        // sftp
//            String ip = "99.1.1.28";
//            String username = "dxprlj02";
//            String pwd = "Lj02@0528";
//            Integer port = 22;

        String ip = "55.14.4.26";
        String username = "qadmsom";
        String pwd = "i@Yk8gK#";
        Integer port = 22;
        Integer timeout = 5000;
        String root = "/ndlj02_local";

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
