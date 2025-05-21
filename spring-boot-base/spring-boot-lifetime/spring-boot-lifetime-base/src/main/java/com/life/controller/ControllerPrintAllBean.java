package com.life.controller;

import com.life.LifeTimeApplication;
import com.life.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;




@Slf4j
@RestController
public class ControllerPrintAllBean {

    @RequestMapping("/allbean")
    public void printBean() {
        // 获取 ApplicationContext 容器中所有的 Bean 名称
        String[] beanNameList = SpringContextHolder.getApplicationContext().getBeanDefinitionNames();
        Arrays.sort(beanNameList);

        // 获取项目package名称
        LifeTimeApplication root = SpringContextHolder.getApplicationContext().getBean(LifeTimeApplication.class);
        String rootPackage = root.getClass().getPackage().getName();

        // 输出所有项目中的 Bean, 不输出Spring原有的
        for (String bean : beanNameList) {
            Class<?> cls = SpringContextHolder.getApplicationContext().getBean(bean).getClass();
            if (cls.getPackage().getName().startsWith(rootPackage)) {
                log.info(bean + " of Type :: " + cls.getName());
            }
        }
    }

}
