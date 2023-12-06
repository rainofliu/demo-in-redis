package com.example.redis.hyperloglog;

import com.example.redis.RedisApplication;
import redis.clients.jedis.Jedis;

/**
 * 垃圾内容过滤demo
 */
public class GarbageContentFilterDemo {

    private Jedis jedis = RedisApplication.getJedis();

    public static void main(String[] args) {
        GarbageContentFilterDemo demo = new GarbageContentFilterDemo();

        String content = "正常的内容";
        System.out.println("是否为垃圾内容：" + (demo.isGarbageContent(content) ? "是" : "否"));

        content = "垃圾内容";
        System.out.println("是否为垃圾内容：" + (demo.isGarbageContent(content) ? "是" : "否"));
        content = "垃圾内容";
        System.out.println("是否为垃圾内容：" + (demo.isGarbageContent(content) ? "是" : "否"));
    }

    private boolean isGarbageContent(String content) {

        return jedis.pfadd("hyperloglog_contennt",content)==0;
    }
}
