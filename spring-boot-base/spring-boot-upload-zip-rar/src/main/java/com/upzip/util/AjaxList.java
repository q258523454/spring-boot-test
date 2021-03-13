package com.upzip.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: zhangj
 * @Date: 2019-10-08
 * @Version 1.0
 */
public class AjaxList<T> {
    private static final Logger logger = LoggerFactory.getLogger(AjaxList.class);
    private boolean isSuccess;
    private T data;

    public AjaxList(boolean isSuccess, T data) {
        this.isSuccess = isSuccess;
        this.data = data;
    }

    public static <T> AjaxList<T> createSuccess(T data) {
        return new AjaxList<T>(true, data);
    }

    public static <T> AjaxList<T> createFail(T data) {
        return new AjaxList<T>(false, data);
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
