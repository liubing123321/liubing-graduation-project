package com.example.interceptor;

import com.example.dao.LoginTicketDAO;
import com.example.dao.UserDAO;
import com.example.model.HostHolder;
import com.example.model.LoginTicket;
import com.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by Administrator on 2017/5/15.
 */
/*拦截器*/ //用户身份的验证
@Component/*泛指组件，当组件不好归类的时候，我们可以使用这个注解进行标注。   */
public class PassportInterceptor implements HandlerInterceptor{
    @Autowired
    LoginTicketDAO loginTicketDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    HostHolder hostHolder;
    //请求开始之前调用
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
       String ticket=null;
        if(httpServletRequest.getCookies()!=null){
                for(Cookie cookie:httpServletRequest.getCookies()){
                        if(cookie.getName().equals("ticket")){
                            ticket=cookie.getValue();
                            break;
                        }
                }
        }

        if(ticket!=null){
            LoginTicket loginTicket=loginTicketDAO.selectByTicket(ticket);
                if(loginTicket==null||loginTicket.getExpired().before(new Date())||loginTicket.getStatus()!=0){
                        return true;/*如果返回false整个拦截器就结束了*/
                }
                User user=userDAO.selectById(loginTicket.getUserId());
                hostHolder.setUser(user);

        }
        return true;
    }
    //handler处理完之后回调
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
            if(modelAndView !=null){
                modelAndView.addObject("user",hostHolder.getUser());//为了能够在所用的页面都能够访问用户
            }
    }
    //整个都处理完了，负责清理工作
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
            hostHolder.clear();
    }
}
