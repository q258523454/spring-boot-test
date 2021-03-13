package com.util.exceptionhandler;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.entity.excpetion.ApiResultMsgUtil;
import com.entity.excpetion.BusinessException;
import com.entity.excpetion.NException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;


/***
 * 所有的 Controller 层的异常的日志记录，都是在这个 GlobalExceptionHandler 中进行记录
 * 优点：将 Controller 层的异常和数据校验的异常进行统一处理，减少模板代码，减少编码量，提升扩展性和可维护性。
 * 缺点：只能处理 Controller 层未捕获（往外抛）的异常，对于 Interceptor（拦截器）层的异常，Spring 框架层的异常，就无能为力了。
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    /**
     * 业务统一异常处理方法
     * ResponseStatus: 指定网络状态码, 这里如果不指定, 那么HttpStatus 默认为=200
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public JSONObject businessException(HttpServletRequest req, BusinessException e) {
        return ApiResultMsgUtil.returnError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), req, e);
    }


    /**
     * 空指针错误
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public JSONObject nullError(HttpServletRequest req, NullPointerException e) {
        return ApiResultMsgUtil.returnError(HttpStatus.INTERNAL_SERVER_ERROR.value(), " 空指针错误", req, e);

    }


    /**
     * JSON格式解析错误
     */
    @ExceptionHandler(value = JSONException.class)
    @ResponseBody
    public JSONObject jsonError(HttpServletRequest req, JSONException e) {
        return ApiResultMsgUtil.returnError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "JSON格式解析错误", req, e);

    }


    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseBody
    public JSONObject missActionParam(HttpServletRequest req, MissingServletRequestParameterException e) {
        return ApiResultMsgUtil.returnError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "接口有参数未传", req, e);

    }

    /**
     * 数字格式错误
     */
    @ExceptionHandler(value = NumberFormatException.class)
    @ResponseBody
    public JSONObject numberFormatError(HttpServletRequest req, NumberFormatException e) {
        return ApiResultMsgUtil.returnError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "数字格式错误", req, e);

    }


    /***
     *  服务器内部错误,全部重定向到 error 页面
     */
    @ExceptionHandler(value = NException.class)
    public String exceptionError(HttpServletRequest req, Exception e) throws NException {
        return "error"; // templates/error.html
    }
}
