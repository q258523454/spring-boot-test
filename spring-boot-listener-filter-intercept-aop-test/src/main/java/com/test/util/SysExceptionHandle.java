package com.test.util;

import com.test.entity.SysException;
import com.test.entity.SysMessage;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created By
 *
 * @author :   zhangjian
 * @date :   2018-08-29
 */


@ControllerAdvice
public class SysExceptionHandle {


    @ExceptionHandler(value = SysException.class)
    @ResponseBody
    public SysMessage sysExceptionExceptionMsg(HttpServletRequest req, SysException e) {
        return SysMessageUtil.error(e.getCode(), e.getMessage(), req.getRequestURL().toString());
    }

    @ExceptionHandler(value = NullPointerException.class)
    public Object nullPointerException() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error.html");
        return modelAndView;
    }

//    @ExceptionHandler(value = Exception.class)
//    @ResponseBody
//    public SysMessage exceptionMsg(HttpServletRequest req, Exception e) {
//        return SysMessageUtil.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), req.getRequestURL().toString());
//    }

}
