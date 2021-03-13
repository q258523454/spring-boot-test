package com.okhttp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.okhttp.entity.HttpReceive;
import com.okhttp.util.OkHttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
public class Controller {

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    @GetMapping("/doPost")
    public String doPost() {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("a", 1);
        jsonObject.put("b", 2);
        jsonObject.put("c", 3);
        String url = "http://localhost:8081/parsePost";

        HttpReceive httpReceive = null;
        try {
            httpReceive = OkHttpUtil.doPost(url, null, jsonObject.toJSONString(), OkHttpUtil.MEDIA_TYPE_JSON);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSON.toJSONString(httpReceive);
    }


    @GetMapping("/doGetAsync")
    public String doGetAsync() {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("a", "1");

        JSONObject header = new JSONObject();
        header.put("aa", "11");

        String url = "http://localhost:8081/parseGetAsync";

        HttpReceive httpReceive = null;
        try {
            httpReceive = OkHttpUtil.doGetAsync(url, header, jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSON.toJSONString(httpReceive);
    }


    @PostMapping(value = "/parsePost", produces = "application/json; charset=UTF-8")
    public @ResponseBody
    String parse(@RequestBody String param, HttpServletRequest httpServletRequest) throws Exception {
        logger.info("Param org :" + param);
        JSONObject response = new JSONObject();
        response.put("msg", "ok");
        return response.toJSONString();
    }

    @GetMapping(value = "/parseGetAsync", produces = "application/json; charset=UTF-8")
    public @ResponseBody
    String parseAsync(HttpServletRequest httpServletRequest) throws Exception {
        logger.info("httpServletRequest param:" + httpServletRequest.getParameter("a"));
        Thread.sleep(3000);
        JSONObject response = new JSONObject();
        response.put("msg", "ok");
        return response.toJSONString();
    }

}
