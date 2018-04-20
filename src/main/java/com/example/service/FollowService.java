package com.example.service;

import com.example.util.JedisAdapter;
import com.example.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/5/28.
 */
@Service
public class FollowService {//通用关注接口
    @Autowired
    JedisAdapter jedisAdapter;

    public boolean follow(int userId,int entityType,int entityId){//关注功能
        String followerKey= RedisKeyUtil.getFollowerKey(entityType,entityId);//某个实体的粉丝key
        String followeeKey=RedisKeyUtil.getFolloweeKey(userId,entityType);//某一个用户关注某一类实体的key
        Date date=new Date();

        Jedis jedis=jedisAdapter.getJedis();//获取jedis
        Transaction tx=jedisAdapter.multi(jedis);//获取jedis事务
        tx.zadd(followerKey,date.getTime(),String.valueOf(userId));//将当前用户放到具体实体的粉丝的集合里，并添加了当前时间以便排序,实体的粉丝增加当前用户
        tx.zadd(followeeKey,date.getTime(),String.valueOf(entityId)); // 当前用户对这类实体关注+1
        List<Object> ret=jedisAdapter.exec(tx,jedis);
        return ret.size()==2 && (Long)ret.get(0)>0 && (Long)ret.get(1)>0;


    }

    public boolean unfollow(int userId,int entityType,int entityId){//取消关注功能
        String followerKey= RedisKeyUtil.getFollowerKey(entityType,entityId);//某个实体的粉丝key
        String followeeKey=RedisKeyUtil.getFolloweeKey(userId,entityType);//某一个用户关注某一类实体的key
        Date date=new Date();

        Jedis jedis=jedisAdapter.getJedis();//获取jedis
        Transaction tx=jedisAdapter.multi(jedis);//获取jedis事务
        tx.zrem(followerKey,String.valueOf(userId));//实体的粉丝删除当前用户
        tx.zrem(followeeKey,String.valueOf(entityId)); // 当前用户对这类实体关注-1
        List<Object> ret=jedisAdapter.exec(tx,jedis);
        return ret.size()==2 && (Long)ret.get(0)>0 && (Long)ret.get(1)>0;


    }

    private List<Integer> getIdsFromSet(Set<String> idset){
        List<Integer> ids=new ArrayList<>();
        for(String str:idset){
            ids.add(Integer.parseInt(str));
        }
        return ids;
    }

    //取出某个实体的所有粉丝
    public List<Integer> getFollowers(int entityType,int entityId,int count){
        String followerKey=RedisKeyUtil.getFollowerKey(entityType,entityId);
        return getIdsFromSet(jedisAdapter.zrevrange(followerKey,0,count));
    }
    public List<Integer> getFollowers(int entityType,int entityId,int offset,int count){//带翻页的
        String followerKey=RedisKeyUtil.getFollowerKey(entityType,entityId);
        return getIdsFromSet(jedisAdapter.zrevrange(followerKey,offset,count));
    }

    //获取关注者
    public List<Integer> getFollowees(int userId,int entityType,int count){
        String followeeKey=RedisKeyUtil.getFolloweeKey(userId,entityType);
        return getIdsFromSet(jedisAdapter.zrevrange(followeeKey,0,count));
    }
    public List<Integer> getFollowees(int userId,int entityType,int offset,int count){//带翻页的
        String followeeKey=RedisKeyUtil.getFolloweeKey(userId,entityType);
        return getIdsFromSet(jedisAdapter.zrevrange(followeeKey,offset,count));
    }
    //获取粉丝数
    public long getFollowerCount(int entityType,int entityId){
        String followerKey=RedisKeyUtil.getFollowerKey(entityType,entityId);
        return jedisAdapter.zcard(followerKey);
    }
    //获取关注者数
    public long getFolloweeCount(int userId,int entityType){
        String followeeKey=RedisKeyUtil.getFolloweeKey(userId,entityType);
        return jedisAdapter.zcard(followeeKey);
    }
    //判断当前用户是否关注了哪个实体
    public boolean isFollower(int userId,int entityType,int entityId){
        String followerKey=RedisKeyUtil.getFollowerKey(entityType,entityId);
        return jedisAdapter.zscore(followerKey,String.valueOf(userId))!=null;
    }
}
