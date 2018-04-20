package com.example.service;

import com.example.controller.LoginController;
import com.example.dao.LoginTicketDAO;
import com.example.dao.UserDAO;
import com.example.model.LoginTicket;
import com.example.model.User;
import com.example.util.WendaUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Administrator on 2017/4/25.
 */
@Service
public class UserService {
    private static final Logger logger= LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public User selectByName(String name) {
        return userDAO.selectByName(name);
    }

    public Map<String,String> register(String username,String password){
            Map<String,String> map=new HashMap<String,String>();
            if(StringUtils.isBlank(username)){
                map.put("msg","用户名不能为空");
            }
            if(StringUtils.isBlank(password)){
                map.put("msg","密码不能为空");
            }
            User user=userDAO.selectByName(username);
            if(user!=null){
                map.put("msg","用户名已经被注册");
                return map;
            }

            user=new User();
            user.setName(username);
            user.setSalt(UUID.randomUUID().toString().substring(0,5));
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",
                            new Random().nextInt(1000)));

            user.setPassword(WendaUtil.MD5(password+user.getSalt()));
            userDAO.addUser(user);


            String ticket=addLoginTicket(user.getId());
            map.put("ticket",ticket);

            return map;
    }

    public Map<String,Object> login(String username,String password){
        Map<String, Object> map = new HashMap<String, Object>();
        if(StringUtils.isBlank(username)){
            map.put("msg","用户名不能为空");
        }
        if(StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
        }
        User user=userDAO.selectByName(username);
        if(user==null){
            map.put("msg","用户名不存在");
            return map;
        }
        if(!WendaUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
            map.put("msg","密码错误");
            return map;
        }

        String ticket=addLoginTicket(user.getId());
        map.put("ticket",ticket);

        map.put("userId",user.getId());
        return map;
    }

    public String addLoginTicket(int userId){
        LoginTicket loginTicket=new LoginTicket();
        loginTicket.setUserId(userId);
        Date now=new Date();
        now.setTime(3600*24*100+now.getTime());
        loginTicket.setExpired(now);//设置过期时间 数据库字段expired存储过期时间
        loginTicket.setStatus(0);//设置状态为有效 字段status表示状态 0代表登录  1代表登出
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDAO.addTicket(loginTicket);
        return loginTicket.getTicket();
    }

    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket,1);
    }
    public User getUser(int id){
        return userDAO.selectById(id);
    }
}
