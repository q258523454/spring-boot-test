package com.filtertest.intercept.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class MyMethodArgumentResolverResolver implements HandlerMethodArgumentResolver {

    /**
     * 作用: 对Controller的入参进行获取或处理
     * 当Controller的入参类型为什么类型时候会进行拦截,并进行参数解析Resovler
     * true:拦截后会执行resolveArgument
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("--------------- 参数解析器: MethodArgumentResolver supportsParameter() ---------------");
        return true;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.info("--------------- 参数解析器: MethodArgumentResolver resolveArgument() ---------------");
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        return "resolveArgument " + request.getParameter(parameter.getParameterName());
    }
}
