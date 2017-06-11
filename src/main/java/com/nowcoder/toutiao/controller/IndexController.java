package com.nowcoder.toutiao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by hsw on 2017/5/23.
 */
//@Controller
public class IndexController {

    @RequestMapping(path = {"/","/index"})
    @ResponseBody
    public String Index(){

        return "Hello Nowcoder";
    }

    @RequestMapping(path = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("groupId") String groupId,
                          @PathVariable("userId") String userId,
                          @RequestParam(value= "type",defaultValue = "1") int type){

        return String.format("GID:{%s}, UID{%s}, type:{%d}",groupId,userId,type);
    }
    @RequestMapping(path = {"/vm"})
    public String templateEngine(Model model){
        model.addAttribute("value1","hsw");
        List<String>  colors = Arrays.asList(new String[] {"RED","GREEN","BLUE"});

        Map<String,String>  map = new HashMap<String,String>();
        for(int i =0; i<3; i++ ){
            map.put(String.valueOf(i) , String.valueOf(i*i));
        }
        model.addAttribute("colors" ,colors);
        model.addAttribute("map" ,map);
        return "news";
    }

    @RequestMapping(path = {"/request"})
    @ResponseBody
    public String request(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpSession session) {

        StringBuilder sb = new StringBuilder();
        Enumeration headNames = request.getHeaderNames();
        while (headNames.hasMoreElements()) {
            String name = (String) headNames.nextElement();
            sb.append(name + ":" + request.getHeader(name) + "<br>");
        }

        return sb.toString();
    }



}
