package com.example.async;

import com.alibaba.fastjson.JSONObject;
import com.example.util.JedisAdapter;
import com.example.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by Administrator on 2017/5/24.
 */
@Service
public class EventProducer {//生产者角色 将对象放入队列中
    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel eventModel){
            try{
                String json= JSONObject.toJSONString(eventModel);//将eventModel对象转换成json对象，将eventModel对象序列化存储到redis队列当中。
                String key= RedisKeyUtil.getEventQueueKey();//设置redis的key
                jedisAdapter.lpush(key, json);
                return true;
            }catch(Exception e){
                return false;
        }
    }
}
