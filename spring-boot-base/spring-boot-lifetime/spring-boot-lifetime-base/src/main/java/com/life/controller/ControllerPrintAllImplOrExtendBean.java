package com.life.controller;

import com.life.service.TestService;
import com.life.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author: zhangj
 * @Date: 2019-09-10
 * @Version 1.0
 */


@Slf4j
@RestController
public class ControllerPrintAllImplOrExtendBean {

    @RequestMapping("/implOrExtendBean")
    public void printBean() {
        // 获取容器中所有的实现了接口 TestService 的 bean
        // 同样也可以直接得到 extend 的 bean
        Map<String, TestService> beansOfType = SpringContextHolder.getApplicationContext().getBeansOfType(TestService.class);
        for (Map.Entry<String, TestService> entry : beansOfType.entrySet()) {
            log.info(entry.getKey() + ":" + entry.getValue().getClass().getName());
        }
    }

    @RequestMapping("/implOrExtendBean2")
    public void printBean2() {
        // TODO: 利用反射遍历所有的class文件, 判断是否实现了 TestService 接口
    }

}
