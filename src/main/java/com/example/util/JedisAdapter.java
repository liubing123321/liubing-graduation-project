package com.example.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/5/17.
 */
@Service
public class JedisAdapter implements InitializingBean {/*实现该方法在初始化bean的时候都会执行*/
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);
    private JedisPool pool;

    public static void print(int index,Object obj){
            System.out.println(String.format("%d,%s",index,obj.toString()));
    }
    public static void main(String[] argv){
            Jedis jedis=new Jedis("redis://localho st:6379/9");
            jedis.flushDB();

            //基础get set
            jedis.set("hello","world");
            jedis.rename("hello","newhello");


            jedis.set("pv","100");//在缓存中存访问次数
            jedis.incr("pv");//访问一次数值加一
            jedis.incrBy("pv",5);//访问一次数值加5
            jedis.decrBy("pv",2);//访问一次数值减2
            print(2,jedis.get("pv"));
            print(3,jedis.keys("*"));

        String listName="list";
        jedis.del(listName);
        for(int i=0;i<10;i++){
                jedis.lpush(listName,"a"+String.valueOf(i));
        }
        print(4,jedis.lrange(listName,0,12));
        print(4, jedis.lrange(listName, 0, 3));
        print(5, jedis.llen(listName));
        print(6, jedis.lpop(listName));
        print(7, jedis.llen(listName));
        print(8, jedis.lrange(listName, 2, 6));
        print(9, jedis.lindex(listName, 3));
        print(10, jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER, "a4", "xx"));
        print(10, jedis.linsert(listName, BinaryClient.LIST_POSITION.BEFORE, "a4", "bb"));
        print(11, jedis.lrange(listName, 0 ,12));

        // hash哈希
        String userKey = "userxx";
        jedis.hset(userKey, "name", "jim");
        jedis.hset(userKey, "age", "12");
        jedis.hset(userKey, "phone", "18618181818");
        print(12, jedis.hget(userKey, "name"));
        print(13, jedis.hgetAll(userKey));
        jedis.hdel(userKey, "phone");
        print(14, jedis.hgetAll(userKey));
        print(15, jedis.hexists(userKey, "email"));
        print(16, jedis.hexists(userKey, "age"));
        print(17, jedis.hkeys(userKey));
        print(18, jedis.hvals(userKey));
        jedis.hsetnx(userKey, "school", "zju");
        jedis.hsetnx(userKey, "name", "yxy");//如果不存在才将字段添加进去
        print(19, jedis.hgetAll(userKey));

        // set集合
        String likeKey1 = "commentLike1";
        String likeKey2 = "commentLike2";
        for (int i = 0; i < 10; ++i) {
            jedis.sadd(likeKey1, String.valueOf(i));
            jedis.sadd(likeKey2, String.valueOf(i*i));
        }
        print(20, jedis.smembers(likeKey1));//显示所有成员变量
        print(21, jedis.smembers(likeKey2));
        print(22, jedis.sunion(likeKey1, likeKey2));//求两个集合的并 集合的天生属性是去重
        print(23, jedis.sdiff(likeKey1, likeKey2));//第一个集合有而第二个集合没有的
        print(24, jedis.sinter(likeKey1, likeKey2));//求两个集合的交集
        print(25, jedis.sismember(likeKey1, "12"));//判断是否是集合里面的成员
        print(26, jedis.sismember(likeKey2, "16"));
        jedis.srem(likeKey1, "5");//删除集合中的元素
        print(27, jedis.smembers(likeKey1));
        jedis.smove(likeKey2, likeKey1, "25");//从前一个集合向后一个集合移动元素25
        print(28, jedis.smembers(likeKey1));
        print(29, jedis.scard(likeKey1));//判断集合里有多少元素
        print(291,jedis.srandmember(likeKey1,2));//在集合中随机取两个数

        //优先队列
        String rankKey = "rankKey";
        jedis.zadd(rankKey, 15, "jim");
        jedis.zadd(rankKey, 60, "Ben");
        jedis.zadd(rankKey, 90, "Lee");
        jedis.zadd(rankKey, 75, "Lucy");
        jedis.zadd(rankKey, 80, "Mei");
        print(30, jedis.zcard(rankKey));//打印当前有多少个值
        print(31, jedis.zcount(rankKey, 61, 100));//在61到100之间有多少人
        print(32, jedis.zscore(rankKey, "Lucy"));//查lucy是多少分
        jedis.zincrby(rankKey, 2, "Lucy");//对分数进行增加
        print(33, jedis.zscore(rankKey, "Lucy"));
        jedis.zincrby(rankKey, 2, "Luc");
        print(34, jedis.zscore(rankKey, "Luc"));
        print(35, jedis.zrange(rankKey, 0, 100));//取数，并且已排序
        print(36, jedis.zrange(rankKey, 0, 10));
        print(36, jedis.zrange(rankKey, 1, 3));//分数从小到大排序
        print(36, jedis.zrevrange(rankKey, 1, 3));//分数从大到小排序
        for (Tuple tuple : jedis.zrangeByScoreWithScores(rankKey, "60", "100")) {
            print(37, tuple.getElement() + ":" + String.valueOf(tuple.getScore()));
        }
        print(38, jedis.zrank(rankKey, "Ben"));//查看排名
        print(39, jedis.zrevrank(rankKey, "Ben"));//查看反向排名

        String setKey = "zset";
        jedis.zadd(setKey, 1, "a");
        jedis.zadd(setKey, 1, "b");
        jedis.zadd(setKey, 1, "c");
        jedis.zadd(setKey, 1, "d");
        jedis.zadd(setKey, 1, "e");

        print(40, jedis.zlexcount(setKey, "-", "+"));//当分数相同时查看从负无穷到正无穷一共有多少人
        print(41, jedis.zlexcount(setKey, "(b", "[d"));
        print(42, jedis.zlexcount(setKey, "[b", "[d"));
        jedis.zrem(setKey, "b");//删除b
        print(43, jedis.zrange(setKey, 0, 10));//范围显示
        jedis.zremrangeByLex(setKey, "(c", "+");//根据字典序将c以上的删除
        print(44, jedis.zrange(setKey, 0 ,2));

        /*JedisPool pool = new JedisPool();//连接池
        for (int i = 0; i < 100; ++i) {
            Jedis j = pool.getResource();
            print(45 , j.get("pv"));
            j.close();
        }*/

        User user = new User();//通过序列化和反序列化实现对象缓存
        user.setName("xx");
        user.setPassword("ppp");
        user.setHeadUrl("a.png");
        user.setSalt("salt");
        user.setId(1);
        print(46, JSONObject.toJSONString(user));
        jedis.set("user1", JSONObject.toJSONString(user));//存入对象

        //取出对象
        String value=jedis.get("user1");
        User user2= JSON.parseObject(value,User.class);
        print(47, user2);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pool=new JedisPool("redis://localhost:6379/10");
    }
    /*向集合中添加元素*/
    public long sadd(String key, String value){
        Jedis jedis=null;
            try{
                jedis=pool.getResource();
                return jedis.sadd(key,value);

            }catch(Exception e){
                logger.error("发生异常"+e.getMessage());
            }finally{
                if(jedis !=null){
                    jedis.close();
                }
            }
            return 0;
    }
/*从集合中删除元素*/
    public long srem(String key, String value){
        Jedis jedis=null;
        try{
            jedis=pool.getResource();
            return jedis.srem(key,value);

        }catch(Exception e){
            logger.error("发生异常"+e.getMessage());
        }finally{
            if(jedis !=null){
                jedis.close();
            }
        }
        return 0;
    }
/*判断集合中元素个数*/
    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }
    //判断集合中是否存在该元素
    public boolean sismember(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return false;
    }

    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }
    public Jedis getJedis(){
        return pool.getResource();
    }

    public Transaction multi(Jedis jedis){//开启一个事务
            try{
                return jedis.multi();
            }catch(Exception e){
                logger.error("发生异常"+e.getMessage());
            }
            return null;
    }
    public List<Object> exec(Transaction tx,Jedis jedis){//事务的一个执行
        try{
            return tx.exec();
        }catch(Exception e){
            logger.error("发生异常",e.getMessage());
        }finally{
            if(tx!=null){
                try {
                    tx.close();
                } catch (IOException ioe) {
                    logger.error("发生异常",ioe.getMessage());
                }
            }
            if(jedis!=null){
                jedis.close();
            }
        }
        return null;
    }
    public long zadd(String key, double score, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zadd(key, score, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }
    public long zrem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrem(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public Set<String> zrevrange(String key, int start, int end) {//返回有序集 key 中，指定区间内的成员。   其中成员的位置按 score 值递减(从大到小)来排序。

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrange(key, start, end);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }
    public long zcard(String key) {//表示队列里面有多少个数字

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zcard(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public List<String> lrange(String key, int start, int end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lrange(key, start, end);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public Double zscore(String key,String member) {//判断有没有分值，因为这里的分值是根据时间来的

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zscore(key,member);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }
}
