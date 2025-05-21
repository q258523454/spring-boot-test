package com.filtertest.contoller;

import com.filtertest.intercept.resolver.MyMethodArgumentResolverResolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by
 *
 * @date :   2018-08-27
 */
@Slf4j
@RestController
public class Controller_ArgumentResolver {

    /**
     * 方法必须要有参数,否则
     * {@link MyMethodArgumentResolverResolver}
     * 无法拦截
     */
    @RequestMapping(value = "/arg", method = RequestMethod.GET)
    public String login(String arg) throws Exception {
        log.info("arg is:" + arg);
        return "login success";
    }
}
