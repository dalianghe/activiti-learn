package com.github.dalianghe.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DemoController extends BaseController {

    Logger logger = LoggerFactory.getLogger(DemoController.class);

    @RequestMapping("/login")
    public String login(ModelMap modelMap) {
        modelMap.put("username", "daliang");
        return "login";
    }

    @RequestMapping("/main")
    public String main(ModelMap modelMap) {
        modelMap.put("username", "daliang");
        return "main/main";
    }


    @RequestMapping("/aaa")
    @ResponseBody
    public String aaa(){
        throw new NullPointerException();
        //return retContent(2010, null);
    }

}
