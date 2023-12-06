package com.example.redis.lua;

import com.example.redis.RedisApplication;
import redis.clients.jedis.Jedis;

public class JedisLuaDemo {
    private static Jedis jedis = RedisApplication.getJedis();

    public static void main(String[] args) {
      Object result =  jedis.eval("redis.call('set','test_key','lua_test_value');"+ "redis.call('expire','test_key','10');"+
              "return 'success';");
        System.out.println(result);
    }
}
