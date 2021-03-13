package com.apache.controller;

import com.alibaba.fastjson.JSONObject;
import com.apache.util.HttpUtilServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;

@RestController
public class MyController {

    private static final Logger logger = LoggerFactory.getLogger(MyController.class);

    @RequestMapping(value = "/postData")
    public @ResponseBody
    String postData() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("张三", 18);
        jsonObject.put("王五", 21);


        JSONObject postJsonObject = new JSONObject();
        postJsonObject.put("学生", jsonObject);

        JSONObject reslut = new JSONObject();

        String URL1 = "http://localhost:8081/parseJsonByte?a=1&b=2";
        HttpUtilServiceImpl.INSTANCE.postHttpJsonByte(URL1, postJsonObject, reslut);

        String URL2 = "http://localhost:8081/parseJsonData?a=1&b=2";
        HttpUtilServiceImpl.INSTANCE.postHttpJsonData(URL2, postJsonObject, reslut);


        return "success";
    }


    @PostMapping(value = "/parseJsonByte", produces = "application/json; charset=UTF-8")
    public @ResponseBody
    String parseJsonByte(HttpServletRequest httpServletRequest) throws Exception {
        httpServletRequest.setCharacterEncoding("UTF-8");               // 解决中文乱码问题
        String a = httpServletRequest.getParameter("a");
        String b = httpServletRequest.getParameter("b");
        BufferedReader bufferReader = httpServletRequest.getReader();   // 获取头部参数信息(post数据头部信息,用来读取post数据)
        StringBuffer buffer = new StringBuffer();
        String line = " ";
        while ((line = bufferReader.readLine()) != null) {
            buffer.append(line);
        }
        String results = buffer.toString();
        logger.info("HttpServletRequest Get Param is :" + "a=" + a + ",b=" + b);
        logger.info("HttpServletRequest Get Param is :" + results);

        //System.out.println(postData);
        // 返回到post客户端(这里是本机服务器)的数据, 客户端用connection.getInputStream()获取
        return results;
    }

    @PostMapping(value = "/parseJsonData", produces = "application/json; charset=UTF-8")
    public @ResponseBody
    String parseJsonData(@RequestBody String param, HttpServletRequest httpServletRequest) throws Exception {
        logger.info("Param org :" + param);
        JSONObject response = new JSONObject();
        response.put("msg", "ok");
        return response.toJSONString();
    }

}