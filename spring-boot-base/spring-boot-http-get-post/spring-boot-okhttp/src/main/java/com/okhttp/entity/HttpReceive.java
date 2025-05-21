package com.okhttp.entity;

import okhttp3.Headers;
import okhttp3.ResponseBody;

import java.io.Serializable;
import java.util.Map;

public class HttpReceive implements Serializable {

    private static final long serialVersionUID = 987337056917732069L;

    // 错误信息
    private String errMsg;

    // 状态码
    private Integer statusCode;

    // 响应头
    private Headers headers;

    // 响应内容
    private ResponseBody body;

    private boolean haveDone;

    public HttpReceive() {
        this.haveDone = false;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public ResponseBody getBody() {
        return body;
    }

    public void setBody(ResponseBody body) {
        this.body = body;
    }

    public boolean isHaveDone() {
        return haveDone;
    }

    public void setHaveDone(boolean haveDone) {
        this.haveDone = haveDone;
    }
}
