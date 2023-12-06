package com.example.redis.zset;

import com.example.redis.RedisApplication;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.Set;

/**
 * 新闻推荐案例
 */
public class NewsDemo {

    private static final String NEWS_KEY = "news";

    private Jedis jedis = RedisApplication.getJedis();

    public void addNews(long userId, long timeStamp) {
        jedis.zadd(NEWS_KEY, timeStamp, String.valueOf(userId));
    }

    /**
     * 搜索新闻
     *
     * @param maxTimestamp
     * @param minTimestamp
     * @param index
     * @param count
     * @return
     */
    public Set<Tuple> searchNews(long maxTimestamp, long minTimestamp, int index, int count) {
       return jedis.zrevrangeByScoreWithScores(NEWS_KEY, maxTimestamp, minTimestamp, index, count);
    }

    public static void main(String[] args) throws Exception {
        NewsDemo demo = new NewsDemo();

        for(int i = 0; i < 20; i++) {
            demo.addNews(i + 1, i + 1);
        }

        long maxTimestamp = 18;
        long minTimestamp = 2;

        int pageNo = 1;
        int pageSize = 10;
        int startIndex = (pageNo - 1) * 10;

        Set<Tuple> searchResult = demo.searchNews(
                maxTimestamp, minTimestamp, startIndex, pageSize);

        System.out.println("搜索指定时间范围内的新闻的第一页：" + searchResult);
    }
}
