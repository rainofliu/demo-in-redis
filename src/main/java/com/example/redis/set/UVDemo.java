package com.example.redis.set;

import com.example.redis.RedisApplication;
import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UVDemo {
    private static final String USER_ACCESS_PREFIX = "user_access::";
    private Jedis jedis = RedisApplication.getJedis();

    public void addUserAccess(long userId) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = dateFormat.format(new Date());
        jedis.sadd(USER_ACCESS_PREFIX + format, String.valueOf(userId));
    }

    public long getUV() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = dateFormat.format(new Date());
        return jedis.scard(USER_ACCESS_PREFIX + format);
    }

    public static void main(String[] args) throws Exception {
        UVDemo demo = new UVDemo();

        for (int i = 0; i < 10000; i++) {
            long userId = i + 1;

            for (int j = 0; j < 100; j++) {
                demo.addUserAccess(userId);
            }
        }

        long uv = demo.getUV();
        System.out.println("当日uv为：" + uv);
    }
}
