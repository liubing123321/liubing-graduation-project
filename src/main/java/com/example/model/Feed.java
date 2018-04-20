package com.example.model;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

/**
 * Created by Administrator on 2017/6/22.
 */
public class Feed {//新鲜事的model
    private int id;//新鲜事ID
    private int type;//新鲜事类型
    private int userId;//产生新鲜事的人的ID
    private Date createdDate;//新鲜事产生的时间点
    private String data;//新鲜事数据 JSON格式能存储各种各样的事件
    private JSONObject dataJSON = null;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        dataJSON = JSONObject.parseObject(data);
    }
    public String get(String key) {
        return dataJSON == null ? null : dataJSON.getString(key);
    }
}
