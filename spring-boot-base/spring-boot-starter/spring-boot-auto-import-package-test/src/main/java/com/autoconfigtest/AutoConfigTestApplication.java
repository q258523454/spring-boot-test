package com.autoconfigtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication(excludeName = "com.autoconfig.exclude.ExcludeAutoBean")
/**
 * 特别注意：@SpringBootApplication(scanBasePackages) 和 @ComponentScan  同时使用，只有 @ComponentScan 生效了
 * 例如下面是扫描指定包: com.inter.config.bean, 只包含 com.inter.config.bean.include.* 及其该目录下的子包
 * @ComponentScan(
 *        value = "com.inter.config.bean",
 *        includeFilters = {
 *                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.inter.config.bean.include.*")
 *        },
 *        // useDefaultFilters默认是true,默认会扫描所有的@Component,当不需要扫描@Component时候必须设置为false
 *        useDefaultFilters = false
 */
@ComponentScan(
        basePackages = {
                "com.inter.config.bean",
                "com.autoconfigtest",
        },
        // useDefaultFilters 默认是true,默认会扫描所有的@Component,当不需要扫描@Component时候必须设置为false
        // useDefaultFilters = true;
        excludeFilters = {
                // 排除扫描指定package
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.inter.config.bean.exclude.*")

                // 排除扫描指定类
                // @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = XXXXX.class)
        }
)
public class AutoConfigTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(AutoConfigTestApplication.class, args);
    }
}
