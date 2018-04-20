package com.example.async;

import com.alibaba.fastjson.JSON;
import com.example.util.JedisAdapter;
import com.example.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/24.
 */

@Service

public class EventConsumer implements InitializingBean,ApplicationContextAware{//作为消费者处理队列中的事件,一启动服务就初始化他
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    private Map<EventType, List<EventHandler>> config = new HashMap<EventType, List<EventHandler>>();//从队列里取出EventType，然后找到要处理的一批EventHandler

    private ApplicationContext applicationContext;//应用上下文

    @Autowired
    JedisAdapter jedisAdapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);//系统一起来，找到所有实现了EventHandler的实现类。
        if(beans!=null){
                for(Map.Entry<String,EventHandler> entry:beans.entrySet()){
                        List<EventType> eventTypes=entry.getValue().getSupportEventTypes();
                    for(EventType type:eventTypes){
                        if(!config.containsKey(type)){
                            config.put(type,new ArrayList<EventHandler>());
                        }
                        config.get(type).add(entry.getValue());

                    }
                }
        }


        Thread thread=new Thread(new Runnable(){

            @Override
            public void run() {
                while(true) {
                    String key = RedisKeyUtil.getEventQueueKey();
                    List<String> events = jedisAdapter.brpop(0, key);//取队列最后一个元素

                    for (String message : events) {
                        if (message.equals(key)) {
                            continue;
                        }

                        EventModel eventModel = JSON.parseObject(message, EventModel.class);//反序列化的到eventModel对象
                        if (!config.containsKey(eventModel.getType())) {
                            logger.error("不能识别的事件");
                            continue;
                        }

                        for (EventHandler handler : config.get(eventModel.getType())) {
                            handler.doHandle(eventModel);
                        }
                    }
                }
            }
        });
            thread.start();


    }



    @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
}

