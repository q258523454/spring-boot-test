package com.encryption.entity;

import java.io.Serializable;

/**
 * Created By
 *
 * @author :   zhangj
 * @date :   2019-02-14
 */
public class PlatformIdkey implements Serializable {
    private String platformName;
    private String platformId;
    private String platformKey;

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getPlatformKey() {
        return platformKey;
    }

    public void setPlatformKey(String platformKey) {
        this.platformKey = platformKey;
    }
}
