package com.test.contoller;

import com.alibaba.fastjson.JSONObject;
import com.test.entity.SysGlobalProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by
 *
 * @author :   zhangjian
 * @date :   2018-08-27
 */

@RestController
public class Controller_GlobalParam {
    @Value("${noParam:null}")
    private String noParam;

    @Value("${globalParam:default}")
    private String globalParam;

    @Value("${globalParamPlus:defaultPlus}")
    private String globalParamPlus;

    @Autowired
    private SysGlobalProperties myGlobalProperties;

    @RequestMapping(value = "/helloworld", method = RequestMethod.GET)
    public String helloworld() {
        return "noParam =" + noParam + ", " +
                "globalParam =" + globalParam + ", " +
                "globalParamPlus=" + globalParamPlus + ", " +
                JSONObject.toJSONString(myGlobalProperties);
    }


}
