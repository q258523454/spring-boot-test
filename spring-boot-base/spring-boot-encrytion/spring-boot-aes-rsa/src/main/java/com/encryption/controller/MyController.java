package com.encryption.controller;

import com.alibaba.fastjson.JSONObject;
import com.encryption.entity.Student;
import com.encryption.service.PostDataService;
import com.encryption.util.AESUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By
 *
 * @author :   zhangj
 * @date :   2019-02-14
 */


@RestController
public class MyController {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PostDataService postDataService;


    public static String key = AESUtil.genKey();

    @GetMapping(value = "/encrypt")
    public @ResponseBody
    String encrypt(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        Student student1 = new Student(1, "1", 1);
        Student student2 = new Student(2, "2", 2);
        Student student3 = new Student(3, "3", 3);

        List<Student> list = new ArrayList<>();
        list.add(student1);
        list.add(student2);
        list.add(student3);

        String jsonString = JSONObject.toJSONString(list);
        log.info("jsonString is :" + jsonString);

        String encryptString = AESUtil.encrypt(jsonString, key);
        log.info("encryptString is :" + encryptString);

        JSONObject res = new JSONObject();
        String URL1 = "http://localhost:8081/decrypt";
        postDataService.postHttpStringData(URL1, encryptString, res);

        return "encrypt";
    }

    @PostMapping(value = "/decrypt", produces = "application/json; charset=UTF-8")
    public @ResponseBody
    String decrypt(@RequestBody String param, HttpServletRequest request) throws Exception {
        String platform_id = request.getHeader("platform_id");
        log.info("platform_id is :" + platform_id);
        log.info("receive param is :" + param);
        String decryptString = AESUtil.decrypt(param, key);
        log.info("decryptString is :" + decryptString);

        JSONObject response = new JSONObject();
        response.put("msg", "ok");
        return response.toJSONString();
    }

}
