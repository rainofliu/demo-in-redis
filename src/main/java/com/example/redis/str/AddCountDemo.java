package com.example.redis.str;

import com.example.redis.RedisApplication;
import redis.clients.jedis.Jedis;

/**
 * @author ly
 */
public class AddCountDemo {
    public static void main(String[] args) {
        Jedis jedis = RedisApplication.getJedis();
        // 点赞
        jedis.del("article:1:dianzan");

        for(int i = 0; i < 10; i++) {
            jedis.incr("article:1:dianzan");
        }
        Long dianzanCounter = Long.valueOf(jedis.get("article:1:dianzan"));
        System.out.println("博客的点赞次数为：" + dianzanCounter);

        jedis.decr("article:1:dianzan");
        dianzanCounter = Long.valueOf(jedis.get("article:1:dianzan"));
        System.out.println("再次查看博客的点赞次数为：" + dianzanCounter);
    }
}
