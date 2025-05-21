

package com.filtertest.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyFilterConfig {

    @Bean
    public FilterRegistrationBean<FilterAll> filter1() {
        FilterRegistrationBean<FilterAll> bean = new FilterRegistrationBean<>();
        bean.setFilter(new FilterAll("filterAll"));
        bean.addUrlPatterns("/*");
        bean.setOrder(1);
        return bean;
    }

    /**
     * 只过滤 "/index"
     */
    @Bean("filterSub1")
    public FilterRegistrationBean<FilterSub> filterSub1() {
        FilterRegistrationBean<FilterSub> bean = new FilterRegistrationBean<>();
        bean.setFilter(new FilterSub("filterSub1"));
        // 注意: addUrlPatterns,不要包含'context-path'
        // 如果写成 addUrlPatterns("/zhang/index"), 包含'context-path'是不生效的.
        // bean.addUrlPatterns("/zhang/index"); // 无效
        bean.addUrlPatterns("/index");
        bean.setOrder(2);
        // setName 这个最好指定,否则用同样的类(FilterSub)再次创建filter的时候,会无效
        // 因为 StandardContext.filterConfigs 中是用这个名字作为key值, 只能保存一个
        bean.setName("filterSub1");
        return bean;
    }


    /**
     * 用同样的类再次去创建 Filter Bean
     */
    @Bean("filterSub2")
    public FilterRegistrationBean<FilterSub> filterSub2() {
        FilterRegistrationBean<FilterSub> bean = new FilterRegistrationBean<>();
        bean.setFilter(new FilterSub("filterSub2"));
        bean.addUrlPatterns("/index");
        // 如果不指定, 默认bean的名字为 "filterSub", 如果之前已经存在且没有指定，那么这个不另外取名, 则无效
        // 参考: DynamicRegistrationBean.getOrDeduceName()
        // bean.setName("filterSub2");
        bean.setOrder(2);
        return bean;
    }
}
