package com.nowcoder.toutiao.service;

import com.nowcoder.toutiao.dao.LoginTicketDAO;
import com.nowcoder.toutiao.dao.UserDAO;
import com.nowcoder.toutiao.model.LoginTicket;
import com.nowcoder.toutiao.model.User;
import com.nowcoder.toutiao.util.ToutiaoUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by hsw on 2017/5/24.
 */
@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO ticketDAO;

    /**
     *注册服务
     * @param username
     * @param password
     * @return
     */
    public Map<String ,Object> register(String username, String password){
        Map<String, Object> result = new HashMap<>();

        if(StringUtils.isBlank(username)){
            result.put("msgname" , "用户名不能为空");
            return result;
        }

        if(StringUtils.isBlank(password)){
            result.put("msgpwd" , "密码不能为空");
            return result;
        }
        User user = userDAO.selectByName(username);
        if(user != null){
            result.put("msgname" , "用户名已经被注册");
            return result;
        }

        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setPassword(ToutiaoUtil.MD5(password + user.getSalt()));

        userDAO.addUser(user);

        //登錄功能
        String ticket = addLoginTicket(user.getId());
        result.put("ticket" , ticket);
        return result;

    }

    /**
     * 登录服务
     * @param username
     * @param password
     * @return
     */
    public Map<String ,Object> login(String username, String password){
        Map<String, Object> result = new HashMap<>();

        if(StringUtils.isBlank(username)){
            result.put("msgname" , "用户名不能为空");
            return result;
        }

        if(StringUtils.isBlank(password)){
            result.put("msgpwd" , "密码不能为空");
            return result;
        }
        User user = userDAO.selectByName(username);
        if(user == null){
            result.put("msgname" , "用户名不存在");
            return result;
        }

        if(!ToutiaoUtil.MD5(password + user.getSalt()).equals(user.getPassword())){
            result.put("msgpwd" , "密码不正确");
            return result;
        }

        //登录名和密码匹配给用户下发ticket，并登录
        String ticket = addLoginTicket(user.getId());
        result.put("ticket" , ticket);
        return result;

    }


    public String addLoginTicket(int userId){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime() + 1000*3600*24);
        loginTicket.setExpired(date);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        ticketDAO.addTicket(loginTicket);

        return loginTicket.getTicket();

    }

    public User getUser(int id){
        return userDAO.selectById(id);
    }

    public void logout(String ticket){
        ticketDAO.updateStatus(ticket,1);
    }


}
