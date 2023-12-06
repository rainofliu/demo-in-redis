# demo-in-redis
Redis实战案例

如果有Python基础，也可以参考[《Redis使用手册》](https://weread.qq.com/web/bookDetail/75732070719551157574079)。

案例使用的Redis版本是本地windows 5.0版本，需要设置auth。
```java
    public static Jedis getJedis() {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        jedis.auth("123456");
        return jedis;
    }
```

* str包  String类型
* list包 list类型
* hash包  hash类型
* set包 set类型
* zset包 sortedset类型
> 以此类推