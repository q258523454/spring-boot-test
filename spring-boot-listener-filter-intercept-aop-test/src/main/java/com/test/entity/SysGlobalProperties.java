package com.test.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created By
 *
 * @author :   zhangjian
 * @date :   2018-08-27
 */

@Component
@ConfigurationProperties(prefix = "global.properties")
public class SysGlobalProperties {
    private String projectPath;
    private String projectPort;

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public String getProjectPort() {
        return projectPort;
    }

    public void setProjectPort(String projectPort) {
        this.projectPort = projectPort;
    }
}
