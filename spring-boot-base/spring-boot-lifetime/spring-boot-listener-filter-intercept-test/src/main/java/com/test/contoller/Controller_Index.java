package com.test.contoller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by
 *
 * @date :   2018-08-27
 */

@RestController
public class Controller_Index {
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "ok";
    }


    @GetMapping(value = "/get/parameter")
    public @ResponseBody
    String parameter1(HttpServletRequest request, HttpServletResponse response,
                      @RequestParam("key") String key,
                      @RequestBody String body) {
        System.out.println("Controller " + JSONObject.toJSONString(request.getParameterMap()));
        System.out.println("Controller key name：" + key);
        System.out.println("Controller body：" + JSONObject.toJSONString(body));
        return "ok";
    }

    @PostMapping(value = "/post/parameter")
    public @ResponseBody
    String parameter2(HttpServletRequest request, HttpServletResponse response,
                      @RequestParam("key") String key,
                      @RequestParam("value") String value,
                      @RequestBody String body) {
        System.out.println("Controller :" + JSONObject.toJSONString(request.getParameterMap()));
        System.out.println("Controller key name：" + key);
        System.out.println("Controller body:" + body);
        return "ok";
    }

}
