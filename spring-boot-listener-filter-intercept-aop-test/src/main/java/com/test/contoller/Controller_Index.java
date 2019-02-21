package com.test.contoller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by
 *
 * @author :   zhangjian
 * @date :   2018-08-27
 */

@Controller
public class Controller_Index {
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        System.out.println("index");
        return "index";
    }


    @GetMapping(value = "/testGetMapping")
    public @ResponseBody
    String testPutMapping(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam("key") String key) {
        System.out.println(JSONObject.toJSONString(request.getParameterMap()));
        System.out.println(key);
        return "ok";
    }

    @PostMapping(value = "/testPostMapping")
    public @ResponseBody
    String testPostMapping(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam("key") String key,
                           @RequestParam("value") String value,
                           @RequestBody String param) {
        System.out.println(JSONObject.toJSONString(request.getParameterMap()));
        System.out.println(key);
        System.out.println(value);
        System.out.println(param);
        return "ok";
    }

}
