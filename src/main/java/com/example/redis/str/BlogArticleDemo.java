package com.example.redis.str;

import com.example.redis.RedisApplication;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * 博客网站的文章发布与查看
 *
 * @author ly
 */
public class BlogArticleDemo {

    public static void main(String[] args) {
        Jedis jedis = RedisApplication.getJedis();

        Long publishBlogResult = jedis.msetnx("article:1:title", "Redis案例实战讲解",
                "article:1:content", "学习Redis的msetnx命令 有助于提升网络通信的效率和性能",
                "article:1:author", "ly",
                "article:1:publishTime", "2023-12-04 17:45:03");
        System.out.println("发布博客的结果:" + publishBlogResult);

        List<String> blogInfo = jedis.mget("article:1:title", "article:1:content", "article:1:author", "article:1:publishTime");
        System.out.println("查看博客内容:" + blogInfo);


       String   updateBlogResult = jedis.mset("article:1:title", "Redis案例实战讲解（1）",
                "article:1:content", "学习Redis的msetnx 改进文章");
        System.out.println("修改发布博客的结果:" + updateBlogResult);

        blogInfo = jedis.mget("article:1:title", "article:1:content", "article:1:author", "article:1:publishTime");
        System.out.println("再次查看博客内容:" + blogInfo);

        Long blogLength = jedis.strlen("article:1:content");
        System.out.println("博客的长度统计：" + blogLength);

        String blogContentPreview = jedis.getrange("article:1:content", 0, 5);
        System.out.println("博客内容预览：" + blogContentPreview);

    }
}
