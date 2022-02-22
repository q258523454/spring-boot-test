package com.readfile.config;

import com.readfile.factory.JsonPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

/**
 * json配置加载器
 * 方法二: 直接实现 PropertySourceFactory, 然后用 @PropertySource 指定配置
 *
 * 优点：与实现 PropertySourceLoader 相比, PropertySourceFactory可以指定任意文件名
 */
@Data
@Configuration
@ConfigurationProperties(value = "my4")
@PropertySource(value = "classpath:/config/application-self.json", factory = JsonPropertySourceFactory.class)
public class C_JsonConfig2 {

    private String name;

    private List<String> list;

    @Override
    public String toString() {
        return "C_JsonConfig2{" +
                "name='" + name + '\'' +
                ", list=" + list +
                '}';
    }
}
