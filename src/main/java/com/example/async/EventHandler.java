package com.example.async;

import java.util.List;

/**
 * Created by Administrator on 2017/5/24.
 */
public interface EventHandler {
    void doHandle(EventModel model);//当相应的对象发生，自己处理该对象
    List<EventType> getSupportEventTypes();//明确自己关心那个event
}
