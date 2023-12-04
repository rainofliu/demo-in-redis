package com.example.redis.str;

import com.example.redis.RedisApplication;
import redis.clients.jedis.Jedis;

/**
 * 用户操作日志demo
 *
 * @author ly
 */
public class OperateLogDemo {
    public static void main(String[] args) {
        Jedis jedis = RedisApplication.getJedis();

        jedis.del("operation_log_2020_01_01");
        jedis.setnx("operation_log_2020_01_01", "");

        for (int i = 0; i < 100; i++) {
            jedis.append("operation_log_2020_01_01","第"+i+"次操作\n");
        }

        String operateLog =  jedis.get("operation_log_2020_01_01");

        System.out.println("操作日志:\n"+operateLog);
    }
}
