package com.upzip.scanner.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PackParam {
    private static final Logger logger = LoggerFactory.getLogger(PackParam.class);
    /**
     * 解压密码
     */
    private String password;

    /**
     * 解压文件存储地址
     */
    private String destPath;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDestPath() {
        return destPath;
    }

    public void setDestPath(String destPath) {
        this.destPath = destPath;
    }
}
