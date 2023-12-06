package com.example.redis.bitmap;

import com.example.redis.RedisApplication;
import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 用户操作日志案例
 */
public class UserOperationLogDemo {

    private Jedis jedis = RedisApplication.getJedis();

    /**
     * 记录用户的操作日志
     *
     * @param operation
     * @param userId
     */
    public void recordUserOperationLog(String operation, long userId) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(new Date());
        jedis.setbit("operation::" + operation + "::" + today + "::log",
                userId, String.valueOf(1));
    }

    /**
     * 判断用户今天是否执行过某个操作
     *
     * @param operation
     * @param userId
     * @return
     */
    public Boolean hasOperated(String operation, long userId) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(new Date());
        return jedis.getbit("operation::" + operation + "::" + today + "::log", userId);
    }

    public static void main(String[] args) {
        UserOperationLogDemo demo = new UserOperationLogDemo();

        demo.recordUserOperationLog("操作1", 110);
        System.out.println("用户110是否执行过操作：" + (demo.hasOperated("操作1", 110) ? "是" : "否"));

        System.out.println("用户111是否执行过操作：" + (demo.hasOperated("操作1", 111) ? "是" : "否"));
    }

}
