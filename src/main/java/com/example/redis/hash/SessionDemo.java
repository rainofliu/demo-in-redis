package com.example.redis.hash;

import com.example.redis.RedisApplication;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * @author ly
 */
public class SessionDemo {

    private Jedis jedis = RedisApplication.getJedis();


    public boolean isSessionValid(String token) throws Exception {
        if (!StringUtils.hasText(token)) {
            return false;
        }
        String session = jedis.hget("sessions", "session::" + token);
        if (!StringUtils.hasText(session)) {
            return false;
        }
        String expireTime = jedis.hget("session::expire_time", "session::" + token);
        if (!StringUtils.hasText(expireTime)) {
            return false;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date expireTimeDate = dateFormat.parse(expireTime);

        Date now = new Date();
        return !now.after(expireTimeDate);
    }


    public String login(String username, String password) {
        System.out.println("基于用户名和密码登录：" + username + ", " + password);

        Random random = new Random();
        long userId = random.nextInt() * 100L;
        String token = UUID.randomUUID().toString().replace("-", "");
        initSession(userId, token);
        return token;
    }

    private void initSession(long userId, String token) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, 24);
        Date expireTime = calendar.getTime();
        jedis.hset("sessions", "session::" + token, String.valueOf(userId));
        jedis.hset("session::expire_time", "session::" + token, dateFormat.format(expireTime));
    }

    public static void main(String[] args) throws Exception{
        SessionDemo demo = new SessionDemo();

        boolean isSessionValid = demo.isSessionValid(null);
        System.out.println("第一次访问系统的session校验结果：" + (isSessionValid ? "通过" : "不通过"));

        // 强制性进行登录，获取到token
        String token = demo.login("zhangsan","123456");
        System.out.println("登陆过后拿到令牌：" + token);

        // 第二次再次访问系统，此时是可以访问的
        isSessionValid = demo.isSessionValid(token);
        System.out.println("第二次访问系统的session校验结果：" + (isSessionValid ? "通过" : "不通过"));
    }
}
