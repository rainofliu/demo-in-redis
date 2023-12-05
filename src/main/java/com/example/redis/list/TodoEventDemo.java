package com.example.redis.list;

import com.example.redis.RedisApplication;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ListPosition;

import java.util.List;
import java.util.Random;

/**
 * OA系统代办事项demo
 */
public class TodoEventDemo {

    private static final String TODO_EVENT_KEY_PREFIX = "todo_event::";

    private Jedis jedis = RedisApplication.getJedis();


    public void addEvent(long userId, String todoEvent) {
        jedis.lpush(TODO_EVENT_KEY_PREFIX + userId, todoEvent);
    }

    public List<String> findToDoEventByPage(Long userId, int pageNo, int pageSize) {
        int startIndex = (pageNo - 1) * pageSize;
        int endIndex = pageNo * pageSize - 1;
        return jedis.lrange(TODO_EVENT_KEY_PREFIX + userId, startIndex, endIndex);
    }

    public void insertEvent(Long userId, ListPosition position, String targetTodoEvent, String event) {
        jedis.linsert(TODO_EVENT_KEY_PREFIX + userId, position, targetTodoEvent, event);
    }

    public void updateTodoEvent(Long userId, int index, String event) {
        jedis.lset(TODO_EVENT_KEY_PREFIX + userId, index, event);
    }

    public void finishTodoEvent(Long userId, String event) {
        jedis.lrem(TODO_EVENT_KEY_PREFIX + userId, 0, event);
    }

    public static void main(String[] args) {
        TodoEventDemo demo = new TodoEventDemo();

        // 添加20个待办事项
        long userId = 2;
        for(int i = 0; i < 20; i++) {
            demo.addEvent(userId, "第" + (i + 1) + "个待办事项");
        }

        // 查询第一页待办事项
        int pageNo = 1;
        int pageSize = 10;
        List<String> todoEventPage = demo.findToDoEventByPage(
                userId, pageNo, pageSize);

        System.out.println("第一次查询第一页待办事项......");
        for(String todoEvent :todoEventPage) {
            System.out.println(todoEvent);
        }

        // 插入一个待办事项
        Random random = new Random();
        int index = random.nextInt(todoEventPage.size());
        String targetTodoEvent = todoEventPage.get(index);

        demo.insertEvent(userId, ListPosition.BEFORE,
                targetTodoEvent, "插入的待办事项");
        System.out.println("在" + targetTodoEvent + "前面插入了一个待办事项");

        // 重新分页查询第一页待办事项
        todoEventPage = demo.findToDoEventByPage(
                userId, pageNo, pageSize);

        System.out.println("第二次查询第一页待办事项......");
        for(String todoEvent :todoEventPage) {
            System.out.println(todoEvent);
        }

        // 修改一个待办事项
        index = random.nextInt(todoEventPage.size());
        demo.updateTodoEvent(userId, index, "修改后的待办事项");

        // 完成一个待办事项
        demo.finishTodoEvent(userId, todoEventPage.get(0));

        // 最后查询一次待办事项
        todoEventPage = demo.findToDoEventByPage(
                userId, pageNo, pageSize);

        System.out.println("第三次查询第一页待办事项......");
        for(String todoEvent :todoEventPage) {
            System.out.println(todoEvent);
        }
    }
}
