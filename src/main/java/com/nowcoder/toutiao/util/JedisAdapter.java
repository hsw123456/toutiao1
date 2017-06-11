package com.nowcoder.toutiao.util;


import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * Created by hsw on 2017/6/9.
 */
@Service
public class JedisAdapter implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);
    private JedisPool pool = null;



    /*public static void print(int index, Object o){
        System.out.println(String.format("%d,%s",index,o.toString()));
    }

    public static void main(String[] args){
        Jedis jedis = new Jedis("106.14.183.121");
        print(1,jedis.ping());
        print(2, jedis.keys("*"));
        jedis.setex("hello",15,"world");
        print(3,jedis.get("hello"));
        jedis.set("pv","100");
        jedis.incrBy("pv",5);
        print(4,jedis.incr("pv"));

        //列表操作
        String listName = "listA";
        for(int i =0; i<10 ;++i){
            jedis.lpush(listName,"a" +String.valueOf(i));
        }
        print(5,jedis.lrange(listName,0,10));
        jedis.lpop(listName);
        print(6,jedis.lrange(listName,0,10));
        print(7,jedis.llen(listName));
        print(8,jedis.lindex(listName,3));

        //
        String rankKey = "rankKey";
        jedis.zadd(rankKey,15,"Jim");
        jedis.zadd(rankKey,60,"Ben");
        jedis.zadd(rankKey,90,"Lin");
        jedis.zadd(rankKey,75,"Mei");
        jedis.zadd(rankKey,80,"Lucy");

        print(9,jedis.zcard(rankKey));
        print(9,jedis.zcount(rankKey,61,100));
        print(9,jedis.zscore(rankKey,"Lucy"));
        print(10,jedis.zrange(rankKey,1,3));
        print(10,jedis.zrevrange(rankKey,1,3));

    }*/

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("106.14.183.121", 6379);
    }

    public Jedis getJedis() {
        return pool.getResource();
    }

    public long sadd(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key, value);

        } catch (Exception e) {
            logger.error("发生异常！" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    public long srem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key, value);

        } catch (Exception e) {
            logger.error("发生异常！" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    public boolean sismember(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key, member);

        } catch (Exception e) {
            logger.error("发生异常！" + e.getMessage());
            return false;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);

        } catch (Exception e) {
            logger.error("发生异常！" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return getJedis().get(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    //将对象序列化，和反序列化
    public void setObject(String key, Object obj) {
            set(key, JSON.toJSONString(obj));

    }

    public <T> T getObject(String key,Class<T> clazz){
        String value = get(key);
        if(value != null){
            return JSON.parseObject(value,clazz);
        }
        return null;
    }

    //包装redis 的lpush
    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    //包装redis 的brpop
    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


}
