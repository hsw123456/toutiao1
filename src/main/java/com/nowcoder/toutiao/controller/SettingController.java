package com.nowcoder.toutiao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by hsw on 2017/5/24.
 */
@Controller
public class SettingController {

    @RequestMapping("/setting")
    @ResponseBody
    public String setting(){
        return "Setting :OK";
    }
}
