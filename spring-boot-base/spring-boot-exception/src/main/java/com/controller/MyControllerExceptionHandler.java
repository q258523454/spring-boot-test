package com.inter.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.inter.entity.excpetion.ApiResultMsgUtil;
import com.inter.entity.excpetion.BusinessException;
import com.inter.entity.excpetion.NException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MyControllerExceptionHandler {

    // 拦截器配置路径: java/com/com.util/GlobalExceptionHandler.java

    // 正常调用
    @GetMapping(value = "/Success")
    public @ResponseBody
    String success() {
        return JSON.toJSONString(ApiResultMsgUtil.returnSuccess("成功"));
    }

    // 测试自定义业务异常类
    @GetMapping(value = "/BusinessException")
    public @ResponseBody
    String businessException() throws BusinessException {
        throw new BusinessException(500, "自定义业务逻辑异常");
    }

    // 测试Json解析异常
    @GetMapping(value = "/JSONException")
    public @ResponseBody
    String jsonException() {
        throw new JSONException();
    }

    // 测试普通异常: 页面重定向到 error.html
    @GetMapping(value = "/Exception")
    public @ResponseBody
    String exception() throws NException {
        throw new NException("error");
    }

    @GetMapping(value = "/ResponseStatus")
    public String responseStatus() {
        return "abc"; // 没有改页面, 出现excpetion错误, 会自动执行 ControllerExceptionHandler.exceptionError
    }

}