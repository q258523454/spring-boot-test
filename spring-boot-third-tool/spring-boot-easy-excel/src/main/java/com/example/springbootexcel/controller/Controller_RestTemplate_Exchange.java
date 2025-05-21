package com.example.springbootexcel.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @date 2022-07-28 11:26
 */

@Slf4j
@RestController
public class Controller_RestTemplate_Exchange {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Environment environment;


    /**
     * 同时发送 body 和 params
     */
    @GetMapping(value = "/restTemplate/post/body/param/1")
    public String restTemplate() {
        String port = environment.getRequiredProperty("server.port");
        String localUrl = "http://localhost:" + port + "/restTemplate/post?id={0}";
        HttpHeaders headers = new HttpHeaders();
        List<String> body = new ArrayList<>();
        body.add("zhangsan");
        body.add("lisi");
        HttpEntity<Object> httpEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(localUrl, HttpMethod.POST, httpEntity, String.class, "id12334");
        return responseEntity.getBody();
    }

    /**
     * 同时发送 body 和 params
     */
    @GetMapping(value = "/restTemplate/post/body/param/2")
    public String restTemplate2() {

        String port = environment.getRequiredProperty("server.port");
        //  注意map的key要与{key}对应
        String localUrl = "http://localhost:" + port + "/restTemplate/post?id={id}";
        HttpHeaders headers = new HttpHeaders();
        List<String> body = new ArrayList<>();
        body.add("zhangsan");
        body.add("lisi");
        HttpEntity<Object> httpEntity = new HttpEntity<>(body, headers);
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("id", "123123");
        ResponseEntity<String> responseEntity = restTemplate.exchange(localUrl, HttpMethod.POST, httpEntity, String.class, paramMap);
        return responseEntity.getBody();
    }
}
