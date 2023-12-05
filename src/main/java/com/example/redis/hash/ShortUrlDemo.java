package com.example.redis.hash;

import com.example.redis.RedisApplication;
import redis.clients.jedis.Jedis;

/**
 * 短连接的demo
 *
 * @author ly
 */
public class ShortUrlDemo {
    private static final String[] X36_ARRAY = "0,1,2,3,4,5,6,7,8,9,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z".split(",");

    private Jedis jedis = RedisApplication.getJedis();

    public ShortUrlDemo() {
        jedis.set("short_url_seed", "51167890045");
    }

    public String getShortUrl(String url) {
        long shortUrlSeed = jedis.incr("short_url_seed");
        StringBuilder builder = new StringBuilder();
        while (shortUrlSeed > 0) {
            builder.append(X36_ARRAY[(int) (shortUrlSeed % 36)]);
            shortUrlSeed = shortUrlSeed/36;
        }
        String shortUrl = builder.reverse().toString();
        jedis.hset("short_url_access_count", shortUrl, "0");
        jedis.hset("url_mapping", shortUrl, url);
        return shortUrl;
    }

    public void incrementShortUrlAccessCount(String shortUrl) {
        jedis.hincrBy("short_url_access_count", shortUrl, 1);
    }

    public long getShortUrlAccessCount(String shortUrl) {
        return Long.parseLong(jedis.hget("short_url_access_count", shortUrl));
    }

    public static void main(String[] args) throws Exception {
        ShortUrlDemo shortUrlDemo = new ShortUrlDemo();
        String url = "https://www.redis.io/index.html";
        String shortUrl = shortUrlDemo.getShortUrl(url);
        System.out.println("页面上展示的短链接地址:" + shortUrl);

        for (int i = 0; i < 1000; i++) {
            shortUrlDemo.incrementShortUrlAccessCount(shortUrl);
        }

        long shortUrlAccessCount = shortUrlDemo.getShortUrlAccessCount(shortUrl);
        System.out.println("短链接被访问的此时一共是:" + shortUrlAccessCount);

    }
}
