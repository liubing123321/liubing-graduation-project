package com.example.service;

import com.example.util.JedisAdapter;
import com.example.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/5/18.
 */
//将点赞和点踩的业务包装在LikeService
@Service
public class LikeService {
    @Autowired
    JedisAdapter jedisAdapter;

    public long getLikeCount(int entityType,int entityId){
        String likeKey=RedisKeyUtil.getLikeKey(entityType,entityId);
        return jedisAdapter.scard(likeKey);
    }

    public int getLikeStatus(int userId, int entityType,int entityId){
        String likeKey=RedisKeyUtil.getLikeKey(entityType,entityId);
        if(jedisAdapter.sismember(likeKey,String.valueOf(userId))){
                return 1;
        }
        String disLikeKey=RedisKeyUtil.getDisLikeKey(entityType,entityId);
        return jedisAdapter.sismember(disLikeKey,String.valueOf(userId)) ? -1:0;
    }

    public long like(int userId, int entityType,int entityId){//某个人喜欢了某个某个东西，返回有多少人喜欢
            String likeKey= RedisKeyUtil.getLikeKey(entityType,entityId);
            jedisAdapter.sadd(likeKey,String.valueOf(userId));

            String disLikeKey=RedisKeyUtil.getDisLikeKey(entityType,entityId);
            jedisAdapter.srem(disLikeKey,String.valueOf(userId));

            return jedisAdapter.scard(likeKey);
    }

    public long disLike(int userId, int entityType,int entityId){
        String dislikeKey= RedisKeyUtil.getDisLikeKey(entityType,entityId);
        jedisAdapter.sadd(dislikeKey,String.valueOf(userId));

        String likeKey=RedisKeyUtil.getLikeKey(entityType,entityId);
        jedisAdapter.srem(likeKey,String.valueOf(userId));

        return jedisAdapter.scard(likeKey);
    }
}
