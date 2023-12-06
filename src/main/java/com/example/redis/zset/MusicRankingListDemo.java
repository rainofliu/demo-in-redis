package com.example.redis.zset;

import com.example.redis.RedisApplication;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.Set;

/**
 * 音乐排行榜案例
 */
public class MusicRankingListDemo {

    private static final String MUSIC_RANKING_LIST_KEY = "music_ranking_list";

    private Jedis jedis = RedisApplication.getJedis();

    /**
     * 把新的音乐加入到排行榜里去
     *
     * @param songId
     */
    public void addSong(long songId) {
        jedis.zadd(MUSIC_RANKING_LIST_KEY, 0d, String.valueOf(songId));
    }

    /**
     * 增加歌曲的分数
     *
     * @param songId
     * @param score
     */
    public void incrementSongScore(long songId, double score) {
        jedis.zincrby(MUSIC_RANKING_LIST_KEY, score, String.valueOf(Long.valueOf(songId)));
    }

    /**
     * 获取歌曲在排行榜里的排名
     *
     * @param songId
     * @return
     */
    public long getSongRank(long songId) {
        return jedis.zrevrank(MUSIC_RANKING_LIST_KEY, String.valueOf(Long.valueOf(songId)));
    }

    /**
     * 获取音乐排行榜
     *
     * @return
     */
    public Set<Tuple> getMusicRankingList() {
        return jedis.zrevrangeWithScores(MUSIC_RANKING_LIST_KEY, 0, 2);
    }

    public static void main(String[] args) {
        MusicRankingListDemo demo = new MusicRankingListDemo();

        for (int i = 0; i < 20; i++) {
            demo.addSong(i + 1);
        }

        demo.incrementSongScore(5, 3.2);
        demo.incrementSongScore(15, 5.6);
        demo.incrementSongScore(7, 9.6);

        long songRank = demo.getSongRank(5);
        System.out.println("查看id为5的歌曲的排名：" + (songRank + 1));

        Set<Tuple> musicRankingList = demo.getMusicRankingList();
        System.out.println("查看音乐排行榜排名前3个的歌曲：" + musicRankingList);
    }
}
