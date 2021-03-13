package com.sftp_util;

import com.ftp_util.MapSortUtil;
import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Date: 2019-06-19
 * @Version 1.0
 */
public class SFTPClient {

    private final static Logger log = LoggerFactory.getLogger(SFTPClient.class);

    /***
     *
     * @param sftpConfig
     * @return
     */
    public static ChannelSftp connect(SFTPConfig sftpConfig) {
        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp sftp = null;
        Channel channel = null;
        try {
            session = jsch.getSession(sftpConfig.getUsername(), sftpConfig.getHostname(), sftpConfig.getPort());
            session.setConfig("StrictHostKeyChecking", "no");
            session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
            session.setPassword(sftpConfig.getPassword());
            session.connect(sftpConfig.getTimeout());
            channel = session.openChannel("sftp");
            channel.connect(sftpConfig.getTimeout());
            sftp = (ChannelSftp) channel;
            log.info("登录成功");
            return sftp;
        } catch (JSchException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }


    /**
     * 断掉连接
     */
    public static void disConnect(ChannelSftp sftp) {
        try {
            sftp.disconnect();
            sftp.getSession().disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
