package com.example.redis.set;

import com.example.redis.RedisApplication;
import redis.clients.jedis.Jedis;

import java.util.HashSet;
import java.util.Set;

/**
 * 商品搜索案例
 */
public class ProductSearchDemo {
    private Jedis jedis = RedisApplication.getJedis();

    /**
     * 添加商品的时候附带一些关键词
     *
     * @param productId
     * @param keywords
     */
    public void addProduct(long productId, String[] keywords) {
        for (String keyword : keywords) {
            // 关键词 对应哪些商品
            jedis.sadd("keyword::" + keyword + "::products", String.valueOf(productId));
        }

    }

    /**
     * 根据多个关键词搜索商品
     *
     * @param keywords
     * @return
     */
    public Set<String> searchProduct(String[] keywords) {
        Set<String> keywordKeySet = new HashSet<>();
        for (String keyword : keywords) {
            keywordKeySet.add("keyword::" + keyword + "::products");
        }
      return jedis.sinter(keywordKeySet.toArray(new String[0]));
    }

    public static void main(String[] args) {
        ProductSearchDemo demo = new ProductSearchDemo();

        // 添加一批商品
        demo.addProduct(11, new String[]{"手机", "iphone", "潮流"});
        demo.addProduct(12, new String[]{"iphone", "潮流", "炫酷"});
        demo.addProduct(13, new String[]{"iphone", "天蓝色"});

        // 根据关键词搜索商品
        Set<String> searchResult = demo.searchProduct(new String[]{"iphone", "潮流"});
        System.out.println("商品搜索结果为：" + searchResult);
    }
}
