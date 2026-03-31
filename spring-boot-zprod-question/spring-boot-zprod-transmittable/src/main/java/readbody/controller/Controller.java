package readbody.controller;

import lombok.extern.slf4j.Slf4j;
import readbody.service.MyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class Controller {

    @Autowired
    private MyService myService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        log.info("index");
        return "ok";
    }

    @RequestMapping(value = "/aspect", method = RequestMethod.GET)
    public String aspect() {
        log.info("aspect");
        myService.runTest();
        return "aspect";
    }

}
