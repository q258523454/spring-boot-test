package com.life.controller;

import com.life.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;




@Slf4j
@RestController
public class ControllerPrintAllBeanWithAnnotation {

    @RequestMapping("/beansWithAnnotation")
    public void printBean() {
        // 获取 ApplicationContext 容器中所有的 Bean 名称
        Map<String, Object> beansWithAnnotation = SpringContextHolder.getApplicationContext().getBeansWithAnnotation(RestController.class);
        for (Map.Entry<String, Object> entry : beansWithAnnotation.entrySet()) {
            log.info(entry.getKey() + ":" + entry.getValue().getClass().getName());
        }
    }

}
