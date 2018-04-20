package com.example.model;

import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/5/15.
 */
@Component  /*这个对象存放拦截器取出来的对象*/
public class HostHolder {
    private static ThreadLocal<User> users=new ThreadLocal<User>();/*利用本地线程实现多用户访问*/
    public User getUser(){
        return users.get();
    }
    public void setUser(User user){
        users.set(user);
    }
    public void clear(){
        users.remove();
    }

}
