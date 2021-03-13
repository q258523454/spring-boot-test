/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.readfile.loader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * json配置加载器
 * 方法一: 直接实现 PropertySourceLoader
 *
 * 定义后去/META-INF/spring.factories配置启动类加载
 * 缺点：只能用 application.json 命名. 即名字必须固定为:application
 * {@link org.springframework.boot.env.PropertySourceLoader]}
 * {@link org.springframework.boot.env.YamlPropertySourceLoader} 对应 yml/yaml 文件
 * {@link org.springframework.boot.env.PropertiesPropertySourceLoader} 对应 properties/xml 文件
 */
public class JsonPropertySourceLoader implements PropertySourceLoader {

    @Override
    public String[] getFileExtensions() {
        return new String[]{"json"};
    }

    /**
     * 下面的例子以 FastJson 为例
     */
    @Override
    public List<PropertySource<?>> load(String name, Resource resource) throws IOException {
        //从resource即application.json加载字符串
        String jsonStr = readResourceAsString(resource);
        //解析字符串为JSONObject
        JSONObject jsonObject = (JSONObject) JSON.parse(jsonStr);
        //map用于存放属性
        Map<String, Object> map = new HashMap<>();
        jsonObject.forEach((key, value) -> putEntry(map, key, value));
        if (map.isEmpty()) {
            return Collections.emptyList();
        }
        List<PropertySource<?>> originTrackedMapPropertySources = Collections.singletonList(new OriginTrackedMapPropertySource(name, Collections.unmodifiableMap(map)));
        return originTrackedMapPropertySources;
    }


    /***
     * 针对多级的结构
     */
    private void putEntry(Map<String, Object> map, String key, Object value) {
        if (value instanceof JSONObject) {
            ((JSONObject) value).forEach((key1, value1) -> putEntry(map, key + "." + key1, value1));
        } else if (value instanceof JSONArray) {
            map.put(key, convertArray((JSONArray) value));
        } else {
            map.put(key, value);
        }
    }

    /**
     * 你可以根据自身需要定义数组如何解析
     * 将json数组转换为字符串 格式如下: 1,2,3
     */
    private String convertArray(JSONArray array) {
        if (array.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        array.forEach(value -> {
            builder.append(",");
            if (value instanceof JSONObject || value instanceof JSONArray) {
                builder.append(JSON.toJSON(value));
            } else {
                builder.append(value);
            }
        });
        return builder.substring(1);
    }

    /**
     * 将资源加载为一个字符串
     */
    private String readResourceAsString(Resource resource) throws IOException {
        StringBuilder builder = new StringBuilder();
        InputStream in = resource.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            builder.append(str);
        }
        //关闭流
        bufferedReader.close();
        inputStreamReader.close();
        in.close();
        return builder.toString();
    }
}