package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by
 *
 * @date :   2018-08-27
 */

@Controller
public class ContollerIndex {
    @GetMapping("/")
    public String index() throws Exception {
        return "index";
    }
}
