package com.fastdfs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by
 *
 * @author :   zhangjian
 * @date :   2018-08-27
 */

@Controller
public class Contoller_Index {
    @GetMapping("/")
    public String index() throws Exception {
        return "index";
    }
}
