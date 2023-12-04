package com.example.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import redis.clients.jedis.Jedis;

@SpringBootApplication
public class RedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
    }


    public static Jedis getJedis() {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        jedis.auth("123456");
        return jedis;
    }
}
