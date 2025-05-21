package com.annotation;

import com.annotation.annotation.MyAnnotaion;
import com.annotation.util.ExcludeTest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;



// 当扫描第三方依赖包的时候，必须显示的指定当前项目包路径
@SpringBootApplication(
        scanBasePackages = {
                "com.annotation",
                // 通配符 **表示任意多级目录
        })
// 排除扫描指定package或者类
@ComponentScan(excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = ExcludeTest.class)})
@MyAnnotaion
public class AnnotationApplication {
    public static void main(String[] args) {
        SpringApplication.run(AnnotationApplication.class, args);
    }

}