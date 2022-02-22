package com.readfile.factory;

import com.readfile.loader.JsonPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * json配置加载器
 * 方法二: 直接实现 PropertySourceFactory,
 *
 * 优点：与实现 PropertySourceLoader 相比, PropertySourceFactory可以指定任意文件名
 */
public class JsonPropertySourceFactory implements PropertySourceFactory {


    /**
     * name为null的时候,参考写法
     * {@link org.springframework.core.io.support.DefaultPropertySourceFactory}
     */
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        if (Objects.isNull(name)) {
            name = resource.getResource().getDescription();
        }
        JsonPropertySourceLoader jsonPropertySourceLoader = new JsonPropertySourceLoader();
        List<PropertySource<?>> propertySourceList = jsonPropertySourceLoader.load(name, resource.getResource());
        return propertySourceList.get(0);
    }

}
