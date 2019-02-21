package com.encryption.controller;

import com.alibaba.fastjson.JSONObject;
import com.encryption.entity.PlatformIdkey;
import com.encryption.entity.User;
import com.encryption.service.PlatformIdKeyService;
import com.encryption.service.PostDataService;
import com.encryption.utils.AESUtil;
import com.encryption.utils.AES_KEY;
import org.apache.log4j.Logger;
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

    protected Logger log = org.apache.log4j.Logger.getLogger(this.getClass());

    @Autowired
    private PostDataService postDataService;


    @Autowired
    private PlatformIdKeyService platformIdKeyService;


    @Autowired
    private AES_KEY aes_key;

    @GetMapping(value = "aes_key")
    public void aes_key() {
        System.out.println(aes_key.getHandekeji());
    }

    /**
     * 获取用户列表
     */
    @GetMapping("/abc")
    public String abc() {
        List<PlatformIdkey> platformIdkeyList = platformIdKeyService.selectAllPlatformIdkey();
        return JSONObject.toJSONString(platformIdkeyList);
    }


    @GetMapping(value = "/encrypt")
    public @ResponseBody
    String encrypt(@RequestBody String param, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        User user1 = new User(1, "1", 1);
        User user2 = new User(2, "2", 2);
        User user3 = new User(3, "3", 3);

        List<User> list = new ArrayList<>();
        list.add(user1);
        list.add(user2);
        list.add(user3);

        String jsonString = JSONObject.toJSONString(list);
        String encryptString = AESUtil.encrypt(jsonString, aes_key.getHandekeji());
        log.info("jsonString is :" + jsonString);
        log.info("encryptString is :" + encryptString);

        JSONObject res = new JSONObject();
        String URL1 = "http://localhost:8080/decrypt";
        postDataService.postHttpStringData(URL1, encryptString, res);

        return "encrypt";
    }


    @PostMapping(value = "/decrypt", produces = "application/json; charset=UTF-8")
    public @ResponseBody
    String decrypt(@RequestBody String param, HttpServletRequest request) throws Exception {
        String platform_id = request.getHeader("platform_id");
        log.info("platform_id is :" + platform_id);
        log.info("receive param is :" + param);
        String decryptString = AESUtil.decrypt(param, aes_key.getHandekeji());
        log.info("decryptString is :" + decryptString);

        JSONObject response = new JSONObject();
        response.put("msg", "ok");
        return response.toJSONString();
    }

    @PostMapping(value = "/handeUserManager", produces = "application/json; charset=UTF-8")
    public @ResponseBody String handeUserManager(@RequestBody String param, HttpServletRequest request) throws Exception {
        String platform_id = request.getHeader("platform_id");
        String decryptString = AESUtil.decrypt(param, platform_id);
        JSONObject response = new JSONObject();
        response.put("msg", "ok");
        return response.toJSONString();
    }



}
