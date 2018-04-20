package com.example.controller;

import com.example.async.EventModel;
import com.example.async.EventProducer;
import com.example.async.EventType;
import com.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/26.
 */
@Controller
public class LoginController {
    private static final Logger logger= LoggerFactory.getLogger(LoginController.class);
    @Autowired
    UserService userService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path={"/reg/"},method = {RequestMethod.POST})

    public String reg(Model model,
                      @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "next",required = false) String next,
                      HttpServletResponse response) {
        try {
            Map<String, String> map = userService.register(username, password);

             if (map.containsKey("ticket")) {
                Cookie cookie=new Cookie("ticket",map.get("ticket"));
                cookie.setPath("/");
                response.addCookie(cookie);
                 if (StringUtils.isNotBlank(next)) {
                     return "redirect:" + next;
                 }
                return "redirect:/";

            }else{
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }

        } catch (Exception e) {
            logger.error("注册异常" + e.getMessage());
            return "login";
        }
    }


    @RequestMapping(path={"/login/"},method = {RequestMethod.POST})

    public String login(Model model,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "next",required = false) String next,
                        @RequestParam(value="rememberme" , defaultValue="false") boolean rememberme,
                        HttpServletResponse response) {
        try {
            Map<String, Object> map = userService.login(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie=new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                response.addCookie(cookie);

                eventProducer.fireEvent(new EventModel(EventType.LOGIN).setExt("username",username).setExt("email","2691240024@qq.com")
                                        .setActorId((int)map.get("userId")));


                if(StringUtils.isNotBlank(next)){
                    return "redirect:"+next;
                }
                return "redirect:/";
            }else{
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }

        } catch (Exception e) {
            logger.error("登录异常" + e.getMessage());
            return "login";
        }
    }


    @RequestMapping(path={"/reglogin"},method = {RequestMethod.GET})

    public String reg(Model model) {/*, @RequestParam(value = "next",required = false) String next*/
        //model.addAttribute("next",next);
        return "login";
    }

    @RequestMapping(path={"/logout"},method = {RequestMethod.GET})

    public String logout(@CookieValue("ticket") String ticket) {
            userService.logout(ticket);
            return "redirect:/";
    }

}
