package com.entity.excpetion;

import com.alibaba.fastjson.annotation.JSONField;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * Created by
 *
 * @date :   2018-08-17
 */

public class ApiResultMsg<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = -4762181054514075995L;
    private String status;
    private Integer code;
    private String msg;

    // --------- 附加信息 BEGIN ---------
    private String url;
    private T field;
    private String error;
    private String errorMsg;
    private T errorObj;
    @JSONField(serialize = false)
    private transient HttpServletRequest httpServletRequest;      // 网络信息,默认不序列化呈现
    @JSONField(serialize = false)
    private transient HttpServletResponse httpServletResponse;    // 网络信息,默认不序列化呈现
    @JSONField(serialize = false)
    private Exception e;                                // 错误信息,默认不序列化呈现

    // --------- 附加信息 END ---------


    public ApiResultMsg() {

    }

    public ApiResultMsg(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ApiResultMsg(String msg, HttpServletRequest httpServletRequest, Exception e) {
        this.msg = msg;
        this.httpServletRequest = httpServletRequest;
        this.e = e;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public HttpServletResponse getHttpServletResponse() {
        return httpServletResponse;
    }

    public void setHttpServletResponse(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }

    public Exception getE() {
        return e;
    }

    public void setE(Exception e) {
        this.e = e;
    }

    public String getMsg() {
        return msg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public T getField() {
        return field;
    }

    public void setField(T field) {
        this.field = field;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public T getErrorObj() {
        return errorObj;
    }

    public void setErrorObj(T errorObj) {
        this.errorObj = errorObj;
    }
}