package com.example.redis.str;

import com.example.redis.RedisApplication;
import org.springframework.util.StopWatch;
import redis.clients.jedis.Jedis;

/**
 * @author ly
 */
public class UniqueIdGenerator {
    public static void main(String[] args) {
        Jedis jedis = RedisApplication.getJedis();
        jedis.del("order_id_record");

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < 100; i++) {
            Long orderId = jedis.incr("order_id_record");
            System.out.println("生成的第"+(i+1)+"个唯一id是："+orderId);
        }
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }
}
