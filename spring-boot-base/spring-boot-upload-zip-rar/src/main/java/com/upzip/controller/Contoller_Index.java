package com.upzip.scanner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by
 *
 */

@Controller
public class Contoller_Index {
    @GetMapping("/")
    public String index() throws Exception {
        return "index_zip";
    }
}
