package com.example.redis.zset;

import com.example.redis.RedisApplication;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.Set;

/**
 * 推荐其他商品案例
 */
public class RecommendProductDemo {
    private static final String KEY_PREFIX = "continue_purchase_products::";

    private Jedis jedis = RedisApplication.getJedis();

    /**
     * 继续购买商品
     *
     * @param productId
     * @param otherProductId
     */
    public void continuePurchase(long productId, long otherProductId) {
        jedis.zincrby(KEY_PREFIX + productId, 1d, String.valueOf(otherProductId));
    }

    /**
     * 推荐其他人购买过的其他商品
     * @param productId
     * @return
     */
    public Set<Tuple> getRecommendProducts(long productId) {
        return jedis.zrevrangeWithScores(KEY_PREFIX + productId,0,2);
    }

    public static void main(String[] args) {
        RecommendProductDemo demo = new RecommendProductDemo();

        int productId = 1;

        for(int i = 0; i < 20; i++) {
            demo.continuePurchase(productId, i + 2);
        }
        for(int i = 0; i < 3; i++) {
            demo.continuePurchase(productId, i + 2);
        }

        Set<Tuple> recommendProducts = demo.getRecommendProducts(productId);
        System.out.println("推荐其他人购买过的商品：" + recommendProducts);
    }
}
