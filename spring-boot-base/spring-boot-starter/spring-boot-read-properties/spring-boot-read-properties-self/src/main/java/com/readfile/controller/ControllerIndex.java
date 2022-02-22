package com.readfile.controller;

import com.readfile.config.A_YmlConfig;
import com.readfile.config.B_PropertiesConfig;
import com.readfile.config.C_JsonConfig;
import com.readfile.config.C_JsonConfig2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;


@Slf4j
@RestController
public class ControllerIndex {

    @Autowired
    private A_YmlConfig aYmlConfig;

    @Autowired
    private B_PropertiesConfig bPropertiesConfig;

    @Autowired
    private C_JsonConfig cJsonConfig;

    @Autowired
    private C_JsonConfig2 cJsonConfig2;

    @PostConstruct
    public void init() {
        log.info("------------------------------");
        log.info(aYmlConfig.toString());
        log.info(bPropertiesConfig.toString());
        log.info(cJsonConfig.toString());
        log.info(cJsonConfig2.toString());
        log.info("------------------------------");

    }

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

}