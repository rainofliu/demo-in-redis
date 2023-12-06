package com.example.redis.expire;

import com.example.redis.RedisApplication;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

/**
 * 支持超时自动释放的分布式锁案例
 */
public class TimeoutDistributedLockDemo {

    private static Jedis jedis = RedisApplication.getJedis();


    /**
     * 加锁
     * @param key
     * @param value
     */
    public Boolean lock(String key, String value, int timeout) {
        long result = jedis.setnx(key, value);
        jedis.expire(key, timeout);
        return result > 0;
    }

    /**
     * 释放锁，需要判断是否是你自己加的锁，才可以释放
     * @param key
     */
    public Boolean unlock(String key, String value) {
        String currentValue = jedis.get(key);

        Pipeline pipeline = jedis.pipelined();

        try {
            pipeline.watch(key);

            if(currentValue == null || currentValue.equals("")
                    || currentValue.equals("null")) {
                return true;
            }

            if(currentValue.equals(value)) {
                pipeline.multi();
                pipeline.del(key);
                pipeline.exec();
                return true;
            } else {
                return false;
            }
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            pipeline.unwatch();
            pipeline.close();
        }
    }

    public static void main(String[] args) throws Exception {
        TimeoutDistributedLockDemo demo = new TimeoutDistributedLockDemo();

        demo.lock("test_lock", "test_value", 10);

        Thread.sleep(12 * 1000);

        Boolean result = demo.lock("test_lock", "test_value", 10);
        System.out.println("第二次加锁结果：" + (result ? "成功" : "失败"));

        System.out.println("不是你本人能否释放锁：" + (demo.unlock("test_lock", "xxxx") ? "能" : "否"));
        System.out.println("是你本人能否释放锁：" + (demo.unlock("test_lock", "test_value") ? "能" : "否"));
    }

}
