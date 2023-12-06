package com.example.redis.set;

import com.example.redis.RedisApplication;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * 朋友圈点赞案例
 */
public class MomentsDemo {

    private static final String MOMENTS_LIKE_KEY_PREFIX = "moment_like_users::";

    private Jedis jedis = RedisApplication.getJedis();

    /**
     * 用户给微信好友的某一条朋友圈点赞
     *
     * @param userId   用户id
     * @param momentId 朋友圈的id
     */
    public void likeMoment(long userId, long momentId) {
        jedis.sadd(MOMENTS_LIKE_KEY_PREFIX + momentId, String.valueOf(userId));
    }

    /**
     * 用户对好友的某一条朋友圈取消点赞
     *
     * @param userId   用户id
     * @param momentId 朋友圈的id
     */
    public void dislikeMoment(long userId, long momentId) {
        jedis.srem(MOMENTS_LIKE_KEY_PREFIX + momentId, String.valueOf(userId));
    }

    /**
     * @param userId   用户id
     * @param momentId 朋友圈id
     * @return
     */
    public boolean hasLiked(long userId, long momentId) {
        return jedis.sismember(MOMENTS_LIKE_KEY_PREFIX + momentId, String.valueOf(userId));
    }

    /**
     * 获取朋友圈点赞的人
     *
     * @param momentId 朋友圈id
     * @return 点赞的人 id
     */
    public Set<String> getMomentLikedUsers(long momentId) {
        return jedis.smembers(MOMENTS_LIKE_KEY_PREFIX + momentId);
    }

    /**
     * 获取你的一条朋友圈被几个人点赞了
     *
     * @param momentId
     * @return
     */
    public long getMomentLikeUsersCount(long momentId) {
        return jedis.scard(MOMENTS_LIKE_KEY_PREFIX + momentId);
    }

    public static void main(String[] args) {
        MomentsDemo demo = new MomentsDemo();

        // 你的用户id
        long userId = 11;
        // 你的朋友圈id
        long momentId = 151;
        // 你的朋友1的用户id
        long friendId = 12;
        // 你的朋友2的用户id
        long otherFriendId = 13;

        // 你的朋友1对你的朋友圈进行点赞，再取消点赞
        demo.likeMoment(friendId, momentId);
        demo.dislikeMoment(friendId, momentId);
        boolean hasLikedMoment = demo.hasLiked(friendId, momentId);
        System.out.println("朋友1刷朋友圈，看到是否对你的朋友圈点赞过：" + (hasLikedMoment ? "是" : "否"));

        // 你的朋友2对你的朋友圈进行点赞
        demo.likeMoment(otherFriendId, momentId);
        hasLikedMoment = demo.hasLiked(otherFriendId, momentId);
        System.out.println("朋友2刷朋友圈，看到是否对你的朋友圈点赞过：" + (hasLikedMoment ? "是" : "否"));

        // 你自己刷朋友圈，看自己的朋友圈的点赞情况
        Set<String> momentLikeUsers = demo.getMomentLikedUsers(momentId);
        long momentLikeUsersCount = demo.getMomentLikeUsersCount(momentId);
        System.out.println("你自己刷朋友圈，看到自己发的朋友圈被" + momentLikeUsersCount + "个人点赞了，点赞的用户为：" + momentLikeUsers);
    }
}
