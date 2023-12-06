package com.example.redis.set;

import com.example.redis.RedisApplication;
import redis.clients.jedis.Jedis;

import java.util.Set;

public class VoteDemo {
    private static final String VOTE_KEY_PREFIX = "vote_item_users::";

    private Jedis jedis = RedisApplication.getJedis();
    /**
     * 用户给微信好友的某一条朋友圈点赞
     *
     * @param userId   用户id
     * @param voteItemId 的id
     */
    public void vote(long userId, long voteItemId) {
        jedis.sadd(VOTE_KEY_PREFIX + voteItemId, String.valueOf(userId));
    }

    /**
     * 用户对好友的某一条朋友圈取消点赞
     *
     * @param userId   用户id
     * @param voteItemId 的id
     */
    public void unvote(long userId, long voteItemId) {
        jedis.srem(VOTE_KEY_PREFIX + voteItemId, String.valueOf(userId));
    }

    /**
     * @param userId   用户id
     * @param voteItemId id
     * @return
     */
    public boolean hasVoted(long userId, long voteItemId) {
        return jedis.sismember(VOTE_KEY_PREFIX + voteItemId, String.valueOf(userId));
    }

    /**
     * 获取朋友圈点赞的人
     *
     * @param voteItemId id
     * @return 点赞的人 id
     */
    public Set<String> getVoteItemUsers(long voteItemId) {
        return jedis.smembers(VOTE_KEY_PREFIX + voteItemId);
    }

    /**
     * 获取你的一条朋友圈被几个人点赞了
     *
     * @param voteItemId
     * @return
     */
    public long getVoteItemUsersCount(long voteItemId) {
        return jedis.scard(VOTE_KEY_PREFIX + voteItemId);
    }


    public static void main(String[] args) {
        VoteDemo demo = new VoteDemo();

        // 定义用户id
        long userId = 1;
        // 定义投票项id
        long voteItemId = 110;

        // 进行投票
        demo.vote(userId, voteItemId);
        // 检查我是否投票过
        boolean hasVoted = demo.hasVoted(userId, voteItemId);
        System.out.println("用户查看自己是否投票过：" +(hasVoted ? "是" : "否"));
        // 归票统计
        Set<String> voteItemUsers = demo.getVoteItemUsers(voteItemId);
        long voteItemUsersCount = demo.getVoteItemUsersCount(voteItemId);
        System.out.println("投票项有哪些人投票：" + voteItemUsers + "，有几个人投票：" + voteItemUsersCount);
    }
}
