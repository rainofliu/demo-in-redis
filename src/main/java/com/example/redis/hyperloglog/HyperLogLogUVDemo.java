package com.example.redis.hyperloglog;

import com.example.redis.RedisApplication;
import org.springframework.util.StopWatch;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HyperLogLogUVDemo {

    private Jedis jedis = RedisApplication.getJedis();

    /**
     * 初始化uv数据
     */
    public void initUVData() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String now = dateFormat.format(new Date());

        Pipeline pipeline = jedis.pipelined();
        for (int i = 0; i < 10000; i++) {
            for (int j = 0; j < 139; j++) {
//                jedis.pfadd("web_site_uv::"+now, String.valueOf((i)+1));
                pipeline.pfadd("web_site_uv::"+now, String.valueOf((i)+1));
            }
        }
        pipeline.sync();
    }


    /**
     * 初始化uv数据
     */
    public void initUVDataNormal() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String now = dateFormat.format(new Date());

        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 139; j++) {
                jedis.pfadd("web_site_uv::"+now, String.valueOf((i)+1));
            }
        }
    }

    public long getUV() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String now = dateFormat.format(new Date());
        return jedis.pfcount("web_site_uv::"+now);
    }

    public static void main(String[] args) {
        HyperLogLogUVDemo demo = new HyperLogLogUVDemo();

        StopWatch stopWatch0 = new StopWatch();
        stopWatch0.start();
        demo.initUVDataNormal();
        stopWatch0.stop();
        System.out.println(stopWatch0.getTotalTimeMillis());

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        demo.initUVData();
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
//        System.out.println(demo.getUV());
    }
}
