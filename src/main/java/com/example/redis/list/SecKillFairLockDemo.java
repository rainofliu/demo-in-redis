package com.example.redis.list;

import com.example.redis.RedisApplication;
import redis.clients.jedis.Jedis;

/**
 * 秒杀活动公平锁
 *
 * @author ly
 */
public class SecKillFairLockDemo {

    private Jedis jedis = RedisApplication.getJedis();

    private static final String REQUEST_QUEUE = "sec_kill_request_queue";

    public void enqueueSecKillRequest(String request) {
        jedis.lpush(REQUEST_QUEUE, request);
    }

    public String dequeueSecKillRequest() {
        return jedis.rpop(REQUEST_QUEUE);
    }

    public static void main(String[] args) {
        SecKillFairLockDemo demo = new SecKillFairLockDemo();

        for (int i = 0; i < 10; i++) {
            demo.enqueueSecKillRequest("第" + (i + 1) + "个秒杀请求");
        }

        while (true) {
            String request = demo.dequeueSecKillRequest();
//            if (request.contains("9")){
//                break;
//            }
            if (request == null || "null".equals(request) || "".equals(request)) {
                break;
            }
            System.out.println(request);
        }

    }
}
