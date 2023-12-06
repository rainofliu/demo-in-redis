package com.example.redis.hyperloglog;

import com.example.redis.RedisApplication;
import com.sun.deploy.net.MessageHeader;
import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.*;

public class WebsiteStatisticsDemo {

    private Jedis jedis = RedisApplication.getJedis();

    /**
     * 初始化某一天的uv数据
     */
    public void initUVData(String date) {
        Random random = new Random();
        int startIndex = random.nextInt(1000);
        System.out.println("今日访问uv起始id为：" + startIndex);

        for (int i = startIndex; i < startIndex + 1358; i++) {
            for (int j = 0; j < 10; j++) {
                jedis.pfadd("hyperloglog_uv_" + date, String.valueOf((i + 1)));
            }
        }
    }

    /**
     * 获取uv值
     *
     * @param date
     * @return
     */
    public long getUV(String date) {
        return jedis.pfcount("hyperloglog_uv_" + date);
    }


    /**
     * 获取周活跃用户数
     *
     * @return
     */
    public long getWeeklyUV() {
        List<String> keys = new LinkedList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        for (int i = 0; i < 7; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            String date = dateFormat.format(calendar.getTime());

            keys.add("hyperloglog_uv_" + date);
        }
        jedis.pfmerge("weekly_uv", keys.toArray(new String[0]));

        return jedis.pfcount("weekly_uv");
    }

    public static void main(String[] args) {
        WebsiteStatisticsDemo demo = new WebsiteStatisticsDemo();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        long duplicateUv = 0;

        for (int i = 0; i < 7; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            String date = dateFormat.format(calendar.getTime());
            demo.initUVData(date);

            long uv = demo.getUV(date);
            System.out.println("日期为" + date + "的uv值为：" + uv);

            duplicateUv += uv;
        }

        long weeklyUV = demo.getWeeklyUV();
        System.out.println("实际的周活跃用户数为：" + weeklyUV);
    }
}
