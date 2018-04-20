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
/*拦截器*/
@Component/*泛指组件，当组件不好归类的时候，我们可以使用这个注解进行标注。   */
public class LoginRequredInterceptor implements HandlerInterceptor{
    @Autowired
    HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if(hostHolder.getUser()==null){
            httpServletResponse.sendRedirect("/reglogin?next="+httpServletRequest.getRequestURI());/*当前页面做为下一次跳转的参数传入*/
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHolder.clear();
    }
}
