package com.example.redis.expire;

import com.example.redis.RedisApplication;
import redis.clients.jedis.Jedis;

/**
 * 数据自动过期的案例
 */
public class ExpireDemo {

    private static Jedis jedis = RedisApplication.getJedis();

    public static void main(String[] args) throws Exception {
        jedis.set("test_key", "test_value");
        jedis.expire("test_key", 10);

        Thread.sleep(12 * 1000);

        String testValue = jedis.get("test_key");
        System.out.println("数据是否过期：" +
                (testValue == null || "null".equals(testValue) ? "是" : "否"));
    }

}
