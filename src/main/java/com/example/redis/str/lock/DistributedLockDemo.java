package com.example.redis.str.lock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import static com.example.redis.RedisApplication.getJedis;

/**
 * 分布式锁demo 不建议用Redis分布式锁 redlock也不稳定
 *
 * @author ly
 */
public class DistributedLockDemo {

    public static void main(String[] args) {
        Jedis jedis = getJedis();
        jedis.set("key1", "value1");
        System.out.println(jedis.get("key1"));

        jedis.del("lock_test");
        // 简单加锁
        String result = jedis.set("lock_test", "value_test", SetParams.setParams().nx());
        System.out.println("第一次加锁的结果:" + result);

        result = jedis.set("lock_test", "value_test", SetParams.setParams().nx());
        System.out.println("第二次加锁的结果：" + result);

        jedis.del("lock_test")
        ;
        result = jedis.set("lock_test", "value_test", SetParams.setParams().nx());
        System.out.println("第三次加锁的结果：" + result);

    }
}
