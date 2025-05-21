package readbody.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by
 *
 * @date :   2018-08-27
 */

@Slf4j
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
        log.info("------------- 控制层 controller -------------");
        log.info("Controller " + JSONObject.toJSONString(request.getParameterMap()));
        log.info("Controller key name：" + key);
        log.info("Controller body：" + body);
        return "ok";
    }

    @PostMapping(value = "/post/parameter")
    public @ResponseBody
    String parameter2(HttpServletRequest request, HttpServletResponse response,
                      @RequestParam("key") String key,
                      @RequestBody String body) {
        log.info("------------- 控制层 controller -------------");
        log.info("Controller :" + JSONObject.toJSONString(request.getParameterMap()));
        log.info("Controller key name：" + key);
        log.info("Controller body:" + body);
        return "ok";
    }

}
