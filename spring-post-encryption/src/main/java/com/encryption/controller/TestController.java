package com.encryption.controller;

import com.alibaba.fastjson.JSONObject;
import com.encryption.entity.User;
import com.encryption.service.PostDataService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By
 *
 * @author :   zhangjian
 * @date :   2018-11-05
 */
@RestController
public class TestController {

    protected Logger log = org.apache.log4j.Logger.getLogger(MyController.class);

    @Autowired
    private PostDataService postDataService;

    @RequestMapping(value = "/postData")
    public @ResponseBody
    String postData() throws Exception {
        User user1 = new User(1, "1", 1);
        User user2 = new User(2, "2", 2);
        User user3 = new User(3, "3", 3);

        List<User> list = new ArrayList<>();
        list.add(user1);
        list.add(user2);
        list.add(user3);
        JSONObject postJsonObject = new JSONObject();
        postJsonObject.put("userListInfo", list);


        JSONObject res = new JSONObject();
        // 本机作为客户端+服务器, 自己给自己post
        String URL1 = "http://localhost:8080/fastJsonParse1?a=1&b=2";
        postDataService.postHttpJsonByte(URL1, postJsonObject, res);

        String URL2 = "http://localhost:8080/fastJsonParse2?a=1&b=2";
        postDataService.postHttpJsonData(URL2, postJsonObject, res);

        return "success";
    }


    @RequestMapping(value = "/fastJsonParse1", produces = "application/json; charset=UTF-8")
    public @ResponseBody
    String fastJsonParse1(HttpServletRequest httpServletRequest) throws Exception {
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
        log.info("HttpServletRequest Get Param is :" + "a=" + a + ",b=" + b);
        log.info("HttpServletRequest Get Param is :" + results);

        //System.out.println(postData);
        // 返回到post客户端(这里是本机服务器)的数据, 客户端用connection.getInputStream()获取
        return results;
    }

    @RequestMapping(value = "/fastJsonParse2", produces = "application/json; charset=UTF-8")
    public @ResponseBody
    String fastJsonParse2(@RequestBody String param, HttpServletRequest httpServletRequest) throws Exception {
        log.info("Param org :" + param);
        JSONObject response = new JSONObject();
        response.put("msg", "ok");
        return response.toJSONString();
    }

    @GetMapping(value = "/rxw")
    public String rxw() {
        return "融信网调用.";
    }

}

